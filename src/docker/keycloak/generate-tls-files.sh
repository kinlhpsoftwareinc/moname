#!/usr/bin/env bash
set -e -o pipefail -u # see `help set`

readonly THIS_PATH=$(readlink --canonicalize "${0}")
readonly THIS_DIR=$(dirname "${THIS_PATH}")
readonly PROJECT_DIRECTORY=$(realpath "${THIS_DIR}/..")
readonly OPTIMIZED_IMAGE=quay.io/keycloak/keycloak:26.1.2-optimized

while [ ${#} -gt 0 ]; do
         case "$1" in
                --dest-path*)
                        if [[ "$1" != *=* ]]; then shift; fi # Value is next arg if no `=`
                        readonly DEST_PATH=$(realpath "${1#*=}")
                        ;;
                --timeout*)
                        if [[ "$1" != *=* ]]; then shift; fi # Value is next arg if no `=`
                        readonly TIMEOUT="${1#*=}"
                        ;;
                -h|--help)
                        printf '\nUsage: generate-tls-files.sh [OPTIONS]\n'
                        printf '\nTODO: Add a description for this\n'
                        printf '\nMandatory arguments to long options are mandatory for short options too.\n'
                        printf '\nOptions:\n'
                        printf '  --dest-path   the local filesystem destination\n'
                        printf '  --timeout     TODO: To be documented\n'
                        printf '  --help        display this help and exit\n'
                        printf "\nRun 'docker container cp --help' for more information\n"
                        exit 0
                        ;;
                *)
                        >&2 printf "generate-tls-files.sh: unrecognized option '${1#}'\n"
                        >&2 printf "Try 'generate-tls-files.sh --help' for more information.\n"
                        exit 2
                        ;;

        esac
        shift
done

if [ -z "${DEST_PATH}" ]; then
        >&2 printf "'generate-tls-files.sh' requires exactly 1 argument.\n"
        >&2 printf "Try 'generate-tls-files.sh --help' for more information.\n"
        exit 1
fi

set +u
if [ -z "${TIMEOUT}" ]; then
        readonly TIMEOUT="70s"
fi
set -u

echo "[1/5] [HTTP Over TLS](https://datatracker.ietf.org/doc/html/rfc2818) -> Removing dated TLS files from ${DEST_PATH}"
rm --force --verbose ${DEST_PATH}/*.{p12,pem}

echo ''
echo "[2/5] Checking build configuration"
set +e
docker compose \
        --project-directory ${PROJECT_DIRECTORY} \
        build \
        --check \
        keycloak
set -e

echo ''
echo "[3/5] [Creating a customized and optimized Keycloak container image](https://www.keycloak.org/server/containers) -> With timeout of ${TIMEOUT}"
docker compose \
        --project-directory ${PROJECT_DIRECTORY} \
        up \
        --build \
        --detach \
        --force-recreate \
        --remove-orphans \
        --renew-anon-volumes \
        keycloak
timeout --preserve-status --verbose ${TIMEOUT} \
        bash -c '
                until [ "$(docker container inspect --format {{.State.Health.Status}} keycloak)" == "healthy" ]
                do
                        sleep .5
                done
        '

echo ''
echo "[4/5] [HTTP Over TLS](https://datatracker.ietf.org/doc/html/rfc2818) -> Copying newest TLS files from keycloak:/opt/keycloak/conf/keystore.{ed25519,rsa}.p12"
mkdir --parents ${DEST_PATH}
for keyalg in ed25519 rsa
do
        docker container cp \
                --archive keycloak:/opt/keycloak/conf/keystore.${keyalg}.p12 \
                ${DEST_PATH}
        cp \
                --archive \
                --force \
                --verbose \
                ${DEST_PATH}/keystore.${keyalg}.p12 \
                ${DEST_PATH}/truststore.${keyalg}.p12
        openssl pkcs12 \
                -in ${DEST_PATH}/keystore.${keyalg}.p12 \
                -nocerts \
                -nodes \
                -out ${DEST_PATH}/private-key.${keyalg}.pem \
                -passin pass:moname
        openssl pkcs12 \
                -in ${DEST_PATH}/keystore.${keyalg}.p12 \
                -nokeys \
                -out ${DEST_PATH}/certificate.${keyalg}.pem \
                -passin pass:moname
done

echo ''
echo '[5/5] Cleaning up at the end'
docker compose \
        --project-directory ${PROJECT_DIRECTORY} \
        down \
        --remove-orphans \
        --volumes \
        keycloak mysql

#docker image rm --force ${OPTIMIZED_IMAGE}

docker image prune --filter 'dangling=true' --force

# clear ; docker compose --project-directory ${PROJECT_DIRECTORY} down --remove-orphans --volumes ; docker system df ; docker buildx prune --all --force ; docker image rm --force ${OPTIMIZED_IMAGE} ; docker image prune --filter 'dangling=true' --force ; docker system df ; docker image ls --all

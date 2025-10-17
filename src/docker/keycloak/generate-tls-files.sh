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
                -h|--help)
                        printf '\nUsage: generate-tls-files.sh [OPTIONS]\n'
                        printf '\nTODO: Add a description for this\n'
                        printf '\nMandatory arguments to long options are mandatory for short options too.\n'
                        printf '\nOptions:\n'
                        printf '  --dest-path   the local filesystem destination\n'
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

echo "[1/7] [HTTP Over TLS](https://datatracker.ietf.org/doc/html/rfc2818) -> Removing dated TLS files from ${DEST_PATH}"
rm --force --verbose ${DEST_PATH}/*.{p12,pem}

echo ''
echo "[2/7] Checking build configuration"
set +e
docker compose \
        --project-directory ${PROJECT_DIRECTORY} \
        build \
        --check \
        keycloak
set -e

echo ''
echo "[3/7] [Building a customized and optimized Keycloak container image](https://www.keycloak.org/server/containers)"
docker compose \
        --project-directory ${PROJECT_DIRECTORY} \
        build \
        keycloak

echo ''
echo "[4/7] Creating a customized and optimized Keycloak container"
CONTAINER_ID="$(docker container create --env-file ${THIS_DIR}/.env --hostname keycloak ${OPTIMIZED_IMAGE})" # ShellCheck (SC2155)
readonly CONTAINER_ID

echo ''
echo "[5/7] Registering a cleanup for the customized and optimized Keycloak container (ID: ${CONTAINER_ID})"
trap 'docker container rm --force --volumes "${CONTAINER_ID}" > /dev/null' EXIT

echo ''
echo "[6/7] [HTTP Over TLS](https://datatracker.ietf.org/doc/html/rfc2818) -> Copying newest TLS files from ${CONTAINER_ID}:/opt/keycloak/conf/keystore.{ed25519,rsa}.p12"
mkdir --parents ${DEST_PATH}
for keyalg in ed25519 rsa
do
        docker container cp \
                --archive ${CONTAINER_ID}:/opt/keycloak/conf/keystore.${keyalg}.p12 \
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
echo '[7/7] Cleaning up at the end'
#docker image rm --force ${OPTIMIZED_IMAGE}

docker image prune --filter 'dangling=true' --force

# clear ; docker compose --project-directory ${PROJECT_DIRECTORY} down --remove-orphans --volumes ; docker system df ; docker buildx prune --all --force ; docker image rm --force ${OPTIMIZED_IMAGE} ; docker image prune --filter 'dangling=true' --force ; docker system df ; docker image ls --all

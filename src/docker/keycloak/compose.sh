#!/usr/bin/env bash
set -e -o pipefail -u # see `help set`

readonly THIS_PATH=$(readlink --canonicalize "${0}")
readonly THIS_DIR=$(dirname "${THIS_PATH}")
readonly PROJECT_DIRECTORY=$(realpath "${THIS_DIR}/..")
readonly DEST_PATH="${PROJECT_DIRECTORY}/../../moname-commons/moname-commons-test/target/classes/oidc/keycloak/tls"

while [ ${#} -gt 0 ]; do
         case "$1" in
                --generate-tls-files)
                        readonly SHOULD_GENERATE_TLS_FILES=true
                        ;;
                -h|--help)
                        printf '\nUsage: keycloak/compose.sh [OPTIONS]\n'
                        printf '\nTODO: Add a description for this\n'
                        printf '\nMandatory arguments to long options are mandatory for short options too.\n'
                        printf '\nOptions:\n'
                        printf '  --generate-tls-files   generate TLS files before creating and starting the Keycloak service container\n'
                        printf '  --help                 display this help and exit\n'
                        printf "\nRun 'generate-tls-files.sh --help' for more information\n"
                        exit 0
                        ;;
                *)
                        >&2 printf "keycloak/compose.sh: unrecognized option '${1#}'\n"
                        >&2 printf "Try 'keycloak/compose.sh --help' for more information.\n"
                        exit 2
                        ;;

        esac
        shift
done

if [ ${SHOULD_GENERATE_TLS_FILES} ]; then
        ${THIS_DIR}/generate-tls-files.sh --dest-path "${DEST_PATH}"
fi

echo ''
echo "Checking build configuration"
set +e
docker compose \
        --project-directory ${PROJECT_DIRECTORY}/ \
        build \
        --check \
        keycloak
set -e

echo "Creating and starting the Keycloak service container"
${PROJECT_DIRECTORY}/compose.sh up keycloak

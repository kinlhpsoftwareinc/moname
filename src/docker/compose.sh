#!/usr/bin/env bash
set -e -o pipefail -u # see `help set`

readonly THIS_PATH=$(readlink --canonicalize "${0}")
readonly THIS_DIR=$(dirname "${THIS_PATH}")

echo "Defining and running service containers as \`--project-directory ${THIS_DIR} $@\`"
docker compose --project-directory ${THIS_DIR} $@

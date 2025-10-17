#!/usr/bin/env bash
./mvnw --activate-profiles test --batch-mode --no-transfer-progress clean

echo "[INFO] "
echo "[INFO] --- find -type f {*.attach_pid*,*.classpath,*.factorypath,*.iml,*.p12,*.pem,*.project} -exec rm '{}' + ---"
find . -type f \( -iname \*.attach_pid\* -o -iname \*.classpath -o -iname \
        \*.factorypath -o -iname \*.iml -o -iname \*.p12 -o -iname \*.pem -o \
        -iname \*.project \) -exec bash -c \
        'echo "[INFO] Deleting $(realpath $1)"' bash '{}' \; -exec rm '{}' +
echo "[INFO] "
echo "[INFO] ------------------------------------------------------------------------"

echo "[INFO] "
echo "[INFO] --- find -type d {*.idea,*.settings,*.vscode} -exec rm -R '{}' + ---"
find . -type d \( -iname \*.idea -o -iname \*.settings -o -iname \*.vscode \) \
        -exec bash -c 'echo "[INFO] Deleting $(realpath $1)"' bash '{}' \; -exec \
        rm -R '{}' +
echo "[INFO] "
echo "[INFO] ------------------------------------------------------------------------"

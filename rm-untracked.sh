#!/usr/bin/env sh
./mvnw --activate-profiles test --batch-mode --no-transfer-progress clean

echo "[INFO] "
echo "[INFO] --- find -type f {*.attach_pid*,*.classpath,*.factorypath,*.iml,*.project} -exec rm '{}' + ---"
find . -type f \( -iname \*.attach_pid\* -o -iname \*.classpath -o -iname \*.factorypath -o -iname \*.iml -o -iname \*.project \) \
        -exec sh -c 'echo "[INFO] Deleting $(realpath $1)"' sh '{}' \; -exec rm '{}' +
echo "[INFO] "
echo "[INFO] ------------------------------------------------------------------------"

echo "[INFO] "
echo "[INFO] --- find -type d {*.idea,*.settings,*.vscode} -exec rm -R '{}' + ---"
find . -type d \( -iname \*.idea -o -iname \*.settings -o -iname \*.vscode \) \
        -exec sh -c 'echo "[INFO] Deleting $(realpath $1)"' sh '{}' \; -exec rm -R '{}' +
echo "[INFO] "
echo "[INFO] ------------------------------------------------------------------------"

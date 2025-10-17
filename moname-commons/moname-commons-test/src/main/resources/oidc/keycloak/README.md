# TODO: Add a description for this

```sh
echo "[HTTP Over TLS](https://datatracker.ietf.org/doc/html/rfc2818) -> Copying newest TLS files from keycloak:/opt/keycloak/conf/keystore.{ed25519,rsa}.p12"
mkdir --parents tls
for keyalg in ed25519 rsa
do
        docker container cp \
                --archive keycloak:/opt/keycloak/conf/keystore.${keyalg}.p12 \
                tls
        cp \
                --archive \
                --force \
                --verbose \
                tls/keystore.${keyalg}.p12 \
                tls/truststore.${keyalg}.p12
        openssl pkcs12 \
                -in tls/keystore.${keyalg}.p12 \
                -nocerts \
                -nodes \
                -out tls/private-key.${keyalg}.pem \
                -passin pass:moname
        openssl pkcs12 \
                -in tls/keystore.${keyalg}.p12 \
                -nokeys \
                -out tls/certificate.${keyalg}.pem \
                -passin pass:moname
done
```

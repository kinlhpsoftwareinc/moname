# TODO: Add a description for this

```sh
./compose.sh [--generate-tls-files]
```

> ***NOTE:*** The `--generate-tls-files` parameter specifies that TLS files will
> also be generated for use.

```sh
curl \
        --data-urlencode client_id=moname-commons-api \
        --data-urlencode client_secret=moname \
        --data-urlencode grant_type=client_credentials \
        --insecure \
        --location https://keycloak:8443/auth/realms/moname/.well-known/openid-configuration \
        --request GET
```

```sh
curl \
        --data-urlencode client_id=moname-commons-api \
        --data-urlencode client_secret=moname \
        --data-urlencode grant_type=client_credentials \
        --insecure \
        --location https://keycloak:8443/auth/realms/moname/protocol/openid-connect/token \
        --request POST
```

```sh
../compose.sh down --remove-orphans --volumes keycloak mysql
```

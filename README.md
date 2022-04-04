# [moname](https://moname.kinlhp.com)

Project to money me with no name.

## Build

### Profiles

| PROFILE | ACTIVE BY DEFAULT | REQUIRES | DESCRIPTION |
|-|-|-|-|
| `hsqldb` | `false` | `test` | Integration tests in the HyperSQL database (in-memory). |
| `mysql` | `false` | `test` | Integration tests in the MySQL database (testcontainers). |
| `test` | `false` | | Compilation running tests. |

### Building all from source

Can be easily built with the
[maven wrapper](https://github.com/takari/maven-wrapper).

```sh
./mvnw --activate-profiles <PROFILES> --batch-mode --no-transfer-progress \
--update-snapshots clean verify
```

> ***NOTE:*** You also need [JDK 17 or above](https://adoptium.net).

> ***NOTE:*** If you want to build with the regular `mvn` command, you will need
[Maven 3.5.0 or above](https://maven.apache.org/docs/history.html).

> ***NOTE:*** You may need to increase the amount of memory available to Maven
> by setting a `MAVEN_OPTS` environment variable with the value `-Xmx512m`.
> Remember to set the corresponding property in your _IDE_ as well if you are
> building and running tests there (e.g. in _Eclipse_ go to
> _Preferences→Java→Installed JREs_ and edit the _JRE_ definition so that all
> processes are launched with those arguments). This property is automatically
> set if you use the maven wrapper.

### Building only one from source

```sh
./mvnw --activate-profiles <PROFILES> <--also-make> <--also-make-dependents> \
--batch-mode --no-transfer-progress --projects com.kinlhp:[REACTOR_PROJECT] \
--update-snapshots clean verify
```

## Docs and refs

### [Maven Model](#maven-model)

* [Descriptor Reference](https://maven.apache.org/ref/current/maven-model/maven.html)

### [Property Expansion](#property-expansion)

* [Automatically Expand Properties at Build Time](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.properties-and-configuration.expand-properties)

### [GMavenPlus](#gmavenplus)

* [Executing a Groovy script](https://github.com/groovy/GMavenPlus/wiki/Usage#executing-a-groovy-script)

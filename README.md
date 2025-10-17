# [moname](https://moname.kinlhp.com)

Project to money me with no name.

## Build

### Profiles

| PROFILE                | ACTIVE BY DEFAULT |            REQUIRES            | DESCRIPTION                                                                                 |
|:-----------------------|:-----------------:|:------------------------------:|:--------------------------------------------------------------------------------------------|
| `hsqldb`               |      `true`       |             `test`             | Integration tests in the JPA layer on HyperSQL database (in-memory).                        |
| `insecurity`           |      `false`      |             `test`             | Integration tests in the API layer (security disabled)                                      |
| `keycloak-optimized`   |      `false`      | `test`, `security` and `mysql` | Integration tests in the API layer on Keycloak OIC provider over HTTPS/TLS (testcontainers) |
| `keycloak-unoptimized` |      `true`       |     `test` and `security`      | Integration tests in the API layer on Keycloak OIC provider (testcontainers)                |
| `mysql`                |      `false`      |             `test`             | Integration tests in the JPA layer on MySQL database (testcontainers).                      |
| `oracle`               |      `false`      |             `test`             | Integration tests in the JPA layer on Oracle database (testcontainers).                     |
| `security`             |      `true`       |             `test`             | Integration tests in the API security layer                                                 |
| `sqlserver`            |      `false`      |             `test`             | Integration tests in the JPA layer on SQL Server database (testcontainers).                 |
| `test`                 |      `true`       |                                | Test the compiled source code.                                                              |

### Building all from source

Can be easily built with the
[maven wrapper](https://github.com/takari/maven-wrapper).

```sh
./mvnw [--activate-profiles <PROFILES>] --batch-mode --no-transfer-progress \
[--threads <THREADS>] --update-snapshots clean verify
```

> ***NOTE:*** You also need [JDK 21 or above](https://adoptium.net).

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
./mvnw [--activate-profiles <PROFILES>] --also-make [--also-make-dependents] \
--batch-mode --no-transfer-progress --projects com.kinlhp:<REACTOR_PROJECT> \
[--threads <THREADS>] --update-snapshots clean verify
```

## Docs and refs

### [Maven](https://maven.apache.org)

#### [Maven Model](#maven-model)

* [Descriptor Reference](https://maven.apache.org/ref/current/maven-model/maven.html)

#### [Property Expansion](#property-expansion)

* [Automatically Expand Properties at Build Time](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.properties-and-configuration.expand-properties)

#### [Central repository](https://repo.maven.apache.org)

### Surefire Plugin

#### [Parallel test execution](#parallel-test-execution)

* [Using JUnit 5 Platform | Running Tests in Parallel](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html#Running_Tests_in_Parallel)

#### [Filtering tests](#filtering-tests)

* [Filtering by Tags](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html#Filtering_by_Tags)

### Failsafe Plugin

#### [Parallel test execution](#parallel-test-execution)

* [Using JUnit 5 Platform | Running Tests in Parallel](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/junit-platform.html#Running_Tests_in_Parallel)

#### [Filtering tests](#filtering-tests)

* [Filtering by Tags](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/junit-platform.html#Filtering_by_Tags)

### [GMavenPlus](#gmavenplus)

* [Executing a Groovy script](https://github.com/groovy/GMavenPlus/wiki/Usage#executing-a-groovy-script)

#### Usage:
```sh
./mvnw --batch-mode --no-transfer-progress --update-snapshots -Ddetail \
-Dplugin=org.codehaus.gmavenplus:gmavenplus-plugin help:describe
```

### [OpenAPITools](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md)

#### [Documentation for the Spring Generator](#openapi-generator-spring-metadata)

* [Metadata](https://openapi-generator.tech/docs/generators/spring/#metadata)

#### [Documentation for the Spring Generator](#openapi-generator-spring-config-options)

* [Config options](https://openapi-generator.tech/docs/generators/spring/#config-options)

#### [Using Templates](https://openapi-generator.tech/docs/templating)

### [MapStruct](https://mapstruct.org/documentation/stable/reference/html/)

* [Configuration options](https://mapstruct.org/documentation/stable/reference/html/#configuration-options)

* [\<compilerArgs\>](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html#compilerArgs)

### Java agent

#### [Mockito](#inline-mock-maker)

* [Explicitly setting up instrumentation for inline mocking (Java 21+)](https://javadoc.io/static/org.mockito/mockito-core/5.18.0/org.mockito/org/mockito/Mockito.html#0.3)

### Suppress warnings

* [javac -XLint](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javac.html#extra-options)

  Usage:
  ```java
  @SuppressWarnings("unused")
  private void foo() {}
  ```

* [SonarLint rules](https://github.com/SonarSource/sonar-java/tree/master/sonar-java-plugin/src/main/resources/org/sonar/l10n/java/rules/java)

  Usage:
  ```java
  @SuppressWarnings("java:S1144")
  private void foo() {}
  ```

### [JaCoCo](#jacoco)

* [Maven Plug-in](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

#### Usage:
```sh
./mvnw --batch-mode --no-transfer-progress --update-snapshots -Ddetail \
-Dplugin=org.jacoco:jacoco-maven-plugin help:describe
```

# [moname](https://moname.kinlhp.com)

Project to money me with no name.

## moname-commons-api

Module for common abstractions of the API layer.

### Building from source

```sh
../../mvnw --activate-profiles <PROFILES_LIST> --also-make --batch-mode --file \
.. --no-transfer-progress --projects com.kinlhp:moname-commons-api \
--threads <THREADS> --update-snapshots clean verify
```

### Reusing commons OpenAPI Specification

`?pageable` parameter _(`in: query`)_ and `problemDetail` response can be reused.

For this, configure plugins in this way:

#### `org.apache.maven.plugins:maven-dependency-plugin`

```xml
<project>
    <properties>
        <moname-commons-api.includes>${moname-commons-api.includes}/components</moname-commons-api.includes>
        <openapi-generator.phase>generate-sources</openapi-generator.phase>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.kinlhp</groupId>
            <artifactId>moname-commons-api</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <!-- # third party # -->
            <!-- maven -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>unpack</id>
                        <phase>${openapi-generator.phase}</phase>
                        <goals>
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>com.kinlhp</groupId>
                                    <artifactId>moname-commons-api</artifactId>
                                    <version>${revision}</version>
                                    <type>jar</type>
                                    <includes>${moname-commons-api.includes}/**</includes>
                                    <outputDirectory>${project.build.outputDirectory}</outputDirectory>
                                    <overWrite>true</overWrite>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

#### `org.apache.maven.plugins:maven-resources-plugin`

```xml
<project>
    <properties>
        <openapi-generator.phase>generate-sources</openapi-generator.phase>
    </properties>
    <build>
        <plugins>
            <!-- # third party # -->
            <!-- maven -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <execution>
                        <id>default-copy-openapi-resources</id>
                        <phase>${openapi-generator.phase}</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.outputDirectory}/openapi</outputDirectory>
                            <resources>
                                <!-- https://maven.apache.org/ref/current/maven-model/maven.html#resource -->
                                <resource>
                                    <!-- OpenAPI specifications -->
                                    <directory>${project.build.resources[0].directory}/openapi</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Besides of course declaring the plugin `org.openapitools:openapi-generator-maven-plugin`

```xml
<project>
    <properties>
        <openapi-generator.input-spec>${project.build.outputDirectory}/openapi/specification.yaml</openapi-generator.input-spec>
        <openapi-generator.phase>generate-sources</openapi-generator.phase>
    </properties>
    <build>
        <plugins>
            <!-- # third party # -->
            <!-- openapitools -->
            <plugin>
                <groupId>org.openapitools</groupId>
                <artifactId>openapi-generator-maven-plugin</artifactId>
                <executions>...</executions>
            </plugin>
        </plugins>
    </build>
</project>
```

## Docs and refs

### [Apache Maven Dependency Plugin](https://maven.apache.org/plugins/maven-dependency-plugin/unpack-mojo.html)

### [Apache Maven Resources Plugin](https://maven.apache.org/plugins/maven-resources-plugin/copy-resources-mojo.html)

### [JSR 380](#jsr-380)

* [Jakarta Bean Validation](https://beanvalidation.org)
  * [Hibernate Validator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single)
* [Jakarta Expression Language](https://projects.eclipse.org/projects/ee4j.el)

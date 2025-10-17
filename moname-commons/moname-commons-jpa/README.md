# [moname](https://moname.kinlhp.com)

Project to money me with no name.

## moname-commons-jpa

Module for common abstractions of the JPA layer.

### Building from source

```sh
../../mvnw [--activate-profiles <PROFILES>] --also-make \
[--also-make-dependents] --batch-mode --file .. --no-transfer-progress \
--projects com.kinlhp:moname-commons-jpa [--threads <THREADS>] \
--update-snapshots clean verify
```

## Docs and refs

### [Hibernate Dialect](#org.hibernate.dialect.Database)

* [Automatic Hibernate Dialect resolution](https://vladmihalcea.com/hibernate-dialect)

### Access strategies

* [Jakarta Data - Persistent Fields](https://jakarta.ee/specifications/data/1.0/data-1.0.0-rc1#_persistent_fields)
* [Hibernate - Access Strategies](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#access)
  * [Overriding the default access strategy](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#_overriding_the_default_access_strategy)
* [Single table inheritance strategy](https://www.baeldung.com/hibernate-inheritance)

### Discriminator column
* [Discriminator column for the SINGLE_TABLE and JOINED Inheritance mapping strategies]()

### [JSR 380](#jsr-380)

* [Jakarta Bean Validation](https://beanvalidation.org)
  * [Hibernate Validator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single)
* [Jakarta Expression Language](https://projects.eclipse.org/projects/ee4j.el)

### [Unified Liquibase `datasource`](#unified-liquibase-datasource)

> ***NOTE:*** The Liquibase `datasource` must be unified with the application's
> `datasource`. Removing `spring.liquibase.url`, `spring.liquibase.user`, and
> `spring.liquibase.password` causes Liquibase to use the primary `datasource`.
> Without a dedicated Liquibase `datasource`, there is no
> `DataSourceClosingSpringLiquibase` (which closes that `datasource` as soon as
> the migration finishes) and no zero-connection window.

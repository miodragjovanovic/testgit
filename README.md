# Template service

## Prerequisites

Java is required to run this project. Java 8 is recommended.

##Quickstart (build this project)

To run this project as is, build database as described in README below, and fix application-*.properties

## Execution

On Windows systems, `gradlew.bat` should be used instead of `gradlew`.

Unit tests will create an embedded postgres instance and build against flyway migration scripts. Related properties are
should be set in application-unitTest.properties, instead of gradle.properties

## Adapting Template For Microservice During Development

1. In "settings.gradle"
    * change project name
1. Refactor package name in "src" files (i.e. com.dais.template )
1. In build.gradle: 
    * add dependencies as needed
1. In src/main/resources/db/migration
    * change initial schema to reflect data model
1. create database as described below
1. In src/test/resources/sample-data/unitTest-data.sql
    * create sample data for embedded database built for unit tests
1. In gradle.properties
    * update credentials that flyway will use to contact running db
1. In application-*.properties
    * update credentials that flyway will use to contact running db, and list feign clients as needed
1. In src/main/docker/Dockerfile
    * change "template" to service name
1. In src/main/groovy/{package}/config/*
    * change references to this package's components, endpoints, entities, etc...
1. For every controller to be created: 
    * create an *Api interface that defines method signature (in src/main/groovy/{package}/domain/api) The response should be the resource's dto (i.e Foo dto)
    * feign interface that extends api (*Client) (in src/main/groovy/{package}/domain/client), 
    * a controller that implements the api (in src/main/groovy/{package}/controller). 
        * controllers should call business service facade (Service) to perform operations. 
1. The services should be contained in (src/main/groovy/{package}/service).
    * the services should call repositories, external apis, or potentially other @services. 
    * each service should have an interface defining its methods. 
1. JPA Entities will be used to model each table defined in schema. 
    * for a given dto-entity pair, methods should be added to DtoMapper (in src/main/groovy/{package}/entity/util)
1. Repositories are interfaces used for data access, and should extend JPA Repository ( allowing for pagination and querying entities)
1. unit tests should be written for the various spring components
1. in local ~/.gradle/gradle.properties: specify artifactory_user, artifactory_password, artifactory_contextUrl, docker_registry_url

## Running locally
To compile and run the Spring Boot application locally:
```
$ gradle bootRun
```

To build the docker container:
```
$ gradle buildDocker
```

To run the docker container locally:
```
$ docker run -it --rm --name template-service -p 8080:8080 template-service
```

To run with specific spring profiles:
```
$ docker run -e "SPRING_PROFILES_ACTIVE=dev" -p 8080:8080 template-service
```

To run with debugging:
```
$ docker run -e "JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 template
```

To test the application while the docker container is running:
```
$ curl -w '\n' -s localhost:8080
$ curl -w '\n' -s localhost:8080/health
```
## Flyway Migrations

All the flyway properties listed in `gradle.properties` can be overridden from the command line.

To run migrations against live postgres, run the following command:

```
./gradlew -Dflyway.url="jdbc:postgresql://target-server:5432/template" -Dflyway.user=foo -Dflyway.password=bar flywayMigrate -i
```

## Database Setup

Run the following queries to setup the service's database

```
CREATE USER <username> WITH  PASSWORD '<password>';
CREATE DATABASE <database name>;
GRANT ALL PRIVILEGES ON DATABASE <database name> TO <user>;
```

Connect to the database

```
CREATE EXTENSION "pgcrypto";
CREATE SCHEMA <schema name> AUTHORIZATION <user>;
ALTER SCHEMA <schema name> OWNER TO <user>;
ALTER ROLE <user> SET search_path TO '<schema name>', 'public';
```


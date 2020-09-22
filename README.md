# Shakespearean Pokemon

REST API that, given a Pokemon name, returns its Shakespearean description. 
The project is based on [Spring Boot 2](https://spring.io/projects/spring-boot) framework.

## Application running

It is possible to run the application in different ways:
* using [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/index.html) 
* creating a jar bundle and running it with java
* building a [Docker](https://www.docker.com/) image and running it as a container

Depending on the above choice, these are the requirements you need to install on the executing environment:
* JDK11+, in first two cases (open source versions are fine, e.g. [AdoptOpenJDK](https://adoptopenjdk.net/))
* Docker 17.05+, for the last one

### Spring Boot Maven Plugin

To run the application using Spring Boot Maven plugin, it is sufficient to run the following command using Maven wrapper available with this project:
```
./mvnw spring-boot:run
```

### Running with Java

To run the application using java, firstly you need to create a jar bundle using Maven:
```
./mvnw clean package
```

And then:
```
java -jar target/shakespearean-pokemon-0.0.1-SNAPSHOT.jar
```

### Docker container

It is possible to build a Docker image using the Dockerfile provided in the root of the project with the following command:
```
docker build -t shakespearean-pokemon .
```

This will produce a Docker image named `shakespearean-pokemon:latest` based on `adoptopenjdk/openjdk11:alpine-jre`. 
Then to run the container:
```
docker run -d -p 8080:8080 shakespearean-pokemon:latest
```


Once application is up and running, it will be possible to invoke the REST endpoint:
```
curl http://localhost:8080/pokemon/charizard
```

Docker image is built using multi-stage builds feature, in order to keep the final image footprint smallest as possible. 

## Testing

Both unit and integration tests are included and their execution is managed inside Maven lifecycle using [Surefire](http://maven.apache.org/surefire/maven-surefire-plugin/) and [Failsafe](http://maven.apache.org/surefire/maven-failsafe-plugin/) maven plugins.

To run unit tests:
```
./mvnw test
```

To run unit and integration tests:

`./mvnw integration-test` or `./mvnw verify`

It is possible to skip unit or integration tests using `-Dskip.unit.test=true` or `-Dskip.integration.test=true`

## Continuous Integration

TBD
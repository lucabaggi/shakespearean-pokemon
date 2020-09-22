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

## Swagger documentation

Swagger documentation is available at http://localhost:8080/swagger-ui/

## Monitoring

The project is enabled with Spring Boot Actuator and Micrometer, exposing in particular a Prometheus endpoint. Take a look at:

* `http://localhost:8080/actuator`

* `http://localhost:8080/actuator/prometheus` 

* `http://localhost:8080/actuator/httptrace`

## Logging

The application logs in Json format using the Logback extension [Logstash logback encoder](https://github.com/logstash/logstash-logback-encoder). This produces a log with the following format:
```
{
  "@timestamp": "2020-09-23T00:30:15.497+02:00",
  "@version": "1",
  "message": "Started ShakespeareanPokemonApplication in 2.704 seconds (JVM running for 3.036)",
  "logger_name": "it.lucabaggi.shakespeareanpokemon.ShakespeareanPokemonApplication",
  "thread_name": "main",
  "level": "INFO",
  "level_value": 20000
}
```
All the json fields above can be used to filter and query logs in Kibana dashboard, if available. 
Other than this, Logstash logback encoder allows to add custom properties using static method `kv` from `StructuredArguments` class, e.g.:
```
import static net.logstash.logback.argument.StructuredArguments.*;
...
log.debug("Response Status code: {}", response.getStatusCode(), kv("request_id", requestId));
```
produces a log with custom field `request_id`:
```
{
  "@timestamp": "2020-09-23T00:30:15.497+02:00",
  "@version": "1",
  "message": "Response Status code: 200 OK",
  "logger_name": "it.lucabaggi.shakespeareanpokemon.RequestResponseLoggingInterceptor",
  "thread_name": "http-nio-8080-exec-1",
  "level": "DEBUG",
  "level_value": 10000,
  "request_id": "0bf0e934-c91a-49fc-b3e9-c5b56231e778"
}
```

## Continuous Integration

TBD
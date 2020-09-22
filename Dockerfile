FROM adoptopenjdk/maven-openjdk11:latest as builder
COPY . ./
RUN mvn clean package -Dskip.unit.test=true -Dskip.integration.test=true

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=builder /target/shakespearean-pokemon-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
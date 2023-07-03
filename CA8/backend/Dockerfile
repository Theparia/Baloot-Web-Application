# Stage 1: Build
#FROM maven:3.8.5-openjdk-17 AS MAVEN
#MAINTAINER BALOOT_DEVELOPER
#COPY pom.xml /build/
#COPY src /build/src/
#WORKDIR /build/
#RUN mvn package


## Stage 2: Package and run
FROM eclipse-temurin:19.0.2_7-jre-jammy
#WORKDIR /app
#COPY --from=MAVEN /build/target/*.jar /app/app.jar
ADD target/CA8-1.0-SNAPSHOT.jar CA8.jar
ENTRYPOINT ["java" , "-jar" , "CA8.jar"]
EXPOSE 8080

#FROM ubuntu:latest
#LABEL authors="lakshaybxt"
#
#ENTRYPOINT ["top", "-b"]

# Build stage
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage

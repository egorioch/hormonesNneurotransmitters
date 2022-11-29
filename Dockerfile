FROM maven:3.6.3-jdk-17 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests
FROM openjdk:17.0.5-oracle
COPY --from=builder /target/hormonesNneurotransmitters-1.0-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
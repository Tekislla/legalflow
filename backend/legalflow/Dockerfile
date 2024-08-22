FROM maven:3.8.4 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk AS runtime
WORKDIR /app
COPY --from=build /app/target/legalflow-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "legalflow-0.0.1-SNAPSHOT.jar"]
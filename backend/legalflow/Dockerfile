FROM maven:3.8.4 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk AS runtime
WORKDIR /app
COPY --from=build /app/target/legalflow-0.0.1-SNAPSHOT.jar /app

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 80

CMD ["java", "-Dserver.port=80", "-jar", "legalflow-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

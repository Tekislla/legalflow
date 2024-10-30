FROM maven:3.8.4 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk AS runtime
WORKDIR /app
COPY --from=build /app/target/legalflow-1.0.jar /app

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 80

CMD ["java", "-Dserver.port=80", "-jar", "legalflow-1.0.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

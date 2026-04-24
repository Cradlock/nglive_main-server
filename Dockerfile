
# Сборка
FROM maven:3.8-openjdk-17 AS build
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests

# Запуск
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/*-fat.jar /app/app.jar
EXPOSE 10000
CMD ["java", "-jar", "/app/app.jar"]

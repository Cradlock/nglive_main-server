# --- Этап 1: Сборка проекта (Build stage) ---
# Используем образ с JDK для сборки
FROM maven:3.9-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Сначала копируем только файл сборки, чтобы закэшировать зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Теперь копируем исходный код и собираем проект
COPY src ./src
RUN mvn clean package -DskipTests

# --- Этап 2: Финальный образ (Runtime stage) ---
# Берем легковесный JRE для запуска
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем только скомпилированный JAR-файл из предыдущего этапа
# Замените 'my-app-fat.jar' на имя вашего файла, который генерирует Maven/Gradle
COPY --from=build /app/target/*-fat.jar app.jar

# Render передает порт через переменную окружения PORT
ENV PORT=10000
EXPOSE 10000

# Запуск приложения. Передаем порт в Vert.x через системную переменную или аргументы
# В коде Vert.x порт нужно брать так: System.getenv().getOrDefault("PORT", "8080")
CMD ["java", "-jar", "app.jar"]



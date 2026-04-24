# --- Этап 1: Сборка (Build stage) ---
FROM gradle:8-jdk17-alpine AS build

WORKDIR /app

# Копируем файлы конфигурации Gradle для кэширования зависимостей
COPY build.gradle ./

# Предварительно скачиваем зависимости (без сборки кода)
RUN gradle dependencies --no-daemon

# Копируем исходный код
COPY src ./src

# Собираем fat-jar
# Обычно это команда shadowJar, если используете соответствующий плагин
RUN gradle shadowJar --no-daemon
RUN ls -l build/libs/


# --- Этап 2: Запуск (Runtime stage) ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# На Render лучше использовать непривилегированного пользователя для безопасности
RUN addgroup -S vertx && adduser -S vertx -G vertx
USER vertx

# Копируем собранный JAR из папки build/libs
# Убедитесь, что имя файла соответствует вашему проекту
COPY --from=build /app/build/libs/server-0.0.1-fat.jar app.jar

# Настройка порта для Render
ENV PORT=10000
EXPOSE 10000

# Настройки памяти для Java в контейнере
ENTRYPOINT ["sh", "-c", "java -Xmx400m -Xms200m -jar app.jar"]



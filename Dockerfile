# Многоэтапная сборка для оптимизации размера образа
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Копируем gradle wrapper и конфигурацию
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Копируем исходный код
COPY src src

# Собираем приложение (пропускаем тесты для ускорения)
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# Финальный образ
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем собранный jar из предыдущего этапа
COPY --from=build /app/build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
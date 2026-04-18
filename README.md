# Tasks Service

## Описание проекта

Сервис для асинхронной обработки текстовых данных.

Позволяет:

* создавать задачи на обработку текста
* отслеживать статус выполнения задачи
* получать результат обработки

Сервис имитирует работу AI-агента: принимает входной текст, обрабатывает его и возвращает результат.

---

## Технологии

* Java 21
* Spring Boot 4
* Spring Web
* Spring Security
* Spring Data JPA
* PostgreSQL
* Docker / Docker Compose
* Swagger (OpenAPI)
* JUnit + Mockito

---

## Архитектура

Проект реализован по слоистой архитектуре:

* Controller — REST API слой
* Service — бизнес-логика и асинхронная обработка
* Repository — работа с БД (Spring Data JPA)
* Entity — модель данных
* DTO — объекты ответов API
* ExceptionHandler — централизованная обработка ошибок

---

## Запуск

### 1. Сборка проекта

mvn clean package

### 2. Запуск приложения

docker-compose up --build

---

## Доступ к сервису

После запуска приложение доступно по адресам:

* REST API: http://localhost:8088
* Swagger UI: http://localhost:8088/swagger-ui/index.html
* OpenAPI JSON: http://localhost:8088/v3/api-docs

---

## Аутентификация и авторизация

Используется HTTP Basic Authentication.

### Пользователи:

| Роль  | Логин | Пароль   |
| ----- | ----- | -------- |
| USER  | user  | password |
| ADMIN | admin | admin    |

---

### Права доступа:

USER:

* POST /tasks — создание задачи

ADMIN:

* GET /tasks/{id} — получение задачи

---

## API

### Создание задачи

POST /tasks

Параметры:

* inputText (query param)

Пример запроса:

curl -X POST "http://localhost:8088/tasks?inputText=hello" -u user:password

Ответ:

{
"taskId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
"status": "CREATED"
}

---

### Получение задачи

GET /tasks/{id}

Пример запроса:

curl -X GET "http://localhost:8088/tasks/{id}" -u admin:admin

Ответ:

{
"taskId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
"inputText": "hello",
"status": "DONE",
"result": "HELLO",
"error": null
}

---

## Асинхронная обработка

Обработка выполняется в отдельном потоке с использованием @Async.

Жизненный цикл задачи:

* CREATED — задача создана
* PROCESSING — выполняется обработка
* DONE — успешно завершена
* FAILED — произошла ошибка

---

## База данных

Используется PostgreSQL.

Таблица: tasks

Поля:

* id (UUID)
* input_text (text)
* status (string)
* result (text)
* error (text)
* created_at (timestamp)
* updated_at (timestamp)

---

## Обработка ошибок

Сервис возвращает ошибки в едином формате:

{
"timestamp": "2026-04-16T12:50:35",
"status": 404,
"error": "Task not found"
}

Коды ответов:

* 400 — некорректный запрос
* 401 — не авторизован
* 403 — недостаточно прав
* 404 — задача не найдена
* 500 — внутренняя ошибка

---

## Тестирование

Запуск тестов:

mvn test

Покрытие включает:

* создание задач
* получение задач
* обработку ошибок
* асинхронную обработку

---

## Принятые компромиссы

* обработка текста реализована как mock (без реального AI)
* используется Basic Auth вместо JWT
* отсутствует очередь сообщений (Kafka/RabbitMQ)
* используется Hibernate auto-ddl вместо миграций
* входные данные передаются как query параметр

---

## Возможные улучшения

* переход на JWT авторизацию
* использование Flyway/Liquibase для миграций
* добавление очередей (Kafka/RabbitMQ)
* поддержка загрузки файлов (multipart)
* добавление кэширования (Redis)
* внедрение мониторинга (Prometheus, Grafana)




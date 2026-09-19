# Finance Tracker 💰

REST API для учёта личных финансов — доходов и расходов.

## Стек технологий

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Функциональность

- Управление пользователями (CRUD)
- Управление транзакциями (доходы/расходы)
- Категории транзакций
- Валидация входных данных
- Глобальная обработка ошибок

## Запуск проекта

### Требования

- Java 21+
- PostgreSQL 14+

### Настройка базы данных

```sql
CREATE DATABASE finance_tracker;
```

### Настройка application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_tracker
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Запуск

```bash
mvn spring-boot:run
```

## API Endpoints

### Users

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/users/{id}` | Получить пользователя |
| POST | `/users` | Создать пользователя |
| PUT | `/users/{id}` | Обновить пользователя |
| DELETE | `/users/{id}` | Удалить пользователя |

### Transactions

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/transaction` | Все транзакции |
| GET | `/transaction/{id}` | Транзакция по ID |
| POST | `/transaction` | Создать транзакцию |
| PUT | `/transaction/{id}` | Обновить транзакцию |
| DELETE | `/transaction/{id}` | Удалить транзакцию |

### Categories

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/categories` | Все категории |

### Statistics

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/statistics/balance/{userId}` | Баланс пользователя |
| GET | `/statistics/income/{userId}` | Сумма доходов |
| GET | `/statistics/expense/{userId}` | Сумма расходов |

## Обработка ошибок API

Приложение использует единый JSON-формат ответа об ошибках — `ApiError`.

Ответ содержит следующие поля:

| Поле | Описание |
|------|----------|
| `timestamp` | Дата и время возникновения ошибки |
| `status` | HTTP-код ответа |
| `message` | Описание ошибки |
| `method` | HTTP-метод запроса |
| `path` | Путь запроса |
| `fieldErrors` | Ошибки валидации, сгруппированные по полям |

### Пользователь не найден — 404 Not Found

Пример ответа на запрос `GET /users/99`, если пользователь не существует:

```json
{
  "timestamp": "2026-09-19T11:30:00",
  "status": 404,
  "message": "Пользователь с ID: 99 не найден",
  "method": "GET",
  "path": "/users/99",
  "fieldErrors": {}
}
```

### Ошибка валидации — 400 Bad Request

При нарушении ограничений Bean Validation поле `fieldErrors` содержит имена полей и списки сообщений об ошибках.

Например, при отправке пустых имени пользователя и пароля:

```json
{
  "timestamp": "2026-09-19T11:30:00",
  "status": 400,
  "message": "Ошибка валидации",
  "method": "POST",
  "path": "/users",
  "fieldErrors": {
    "username": [
      "Имя пользователя не может быть пустым"
    ],
    "password": [
      "Пароль не может быть пустым"
    ]
  }
}
```

У одного поля может быть несколько ошибок, поэтому сообщения представлены списком.

### Внутренняя ошибка — 500 Internal Server Error

При непредвиденной ошибке приложение возвращает общее сообщение, не раскрывая внутренние детали исключения.

```json
{
  "timestamp": "2026-09-19T11:30:00",
  "status": 500,
  "message": "Внутренняя ошибка сервера",
  "method": "GET",
  "path": "/users/99",
  "fieldErrors": {}
}
```

### Текущие ограничения обработки ошибок

Обработка некорректных HTTP-запросов дорабатывается отдельно. В частности, ведётся работа над корректными статусами для невалидного JSON, отсутствующих обязательных параметров, неподдерживаемых HTTP-методов и типов содержимого.

## Автор

[Niko91101](https://github.com/Niko91101)
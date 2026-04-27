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
| GET | /users/{id} | Получить пользователя |
| POST | /users | Создать пользователя |
| PUT | /users/{id} | Обновить пользователя |
| DELETE | /users/{id} | Удалить пользователя |

### Transactions
| Метод | URL | Описание |
|-------|-----|----------|
| GET | /transaction | Все транзакции |
| GET | /transaction/{id} | Транзакция по id |
| POST | /transaction | Создать транзакцию |
| PUT | /transaction/{id} | Обновить транзакцию |
| DELETE | /transaction/{id} | Удалить транзакцию |

### Categories
| Метод | URL | Описание |
|-------|-----|----------|
| GET | /categories | Все категории |

## Автор

[Niko91101](https://github.com/Niko91101)
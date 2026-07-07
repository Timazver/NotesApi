# Personal Notes API --- Техническое задание

## Стек

-   Kotlin
-   Spring Web
-   PostgreSQL
-   Spring Data
-   Swagger
-   Docker
-   VS Code

## Этап 1. Создать проект

Цель: понять структуру ASP.NET Core проекта.

``` bash
dotnet new webapi -n NotesApi
```

Изучить: - Program.cs - appsettings.json - Controllers

Результат: запускается Swagger.

## Этап 2. Поднять PostgreSQL

Цель: научиться работать с БД через Docker.

Создать docker-compose.yml с PostgreSQL 16.

Проверить:

``` bash
docker compose up -d
```

Подключиться через DBeaver, DataGrip или pgAdmin.

Результат: рабочая PostgreSQL.

## Этап 3. Создать сущность Note

Поля:

-   Id
-   Title
-   Content
-   CreatedAt
-   UpdatedAt
-   IsArchived

Результат: есть первая Entity.

## Этап 4. Подключить EF Core

Установить:

``` bash
dotnet add package Microsoft.EntityFrameworkCore
dotnet add package Npgsql.EntityFrameworkCore.PostgreSQL
dotnet add package Microsoft.EntityFrameworkCore.Design
```

Создать AppDbContext и DbSet
```{=html}
<Note>
```
.

Результат: приложение подключается к PostgreSQL.

## Этап 5. Сделать первую миграцию

``` bash
dotnet ef migrations add InitialCreate
dotnet ef database update
```

Результат: таблица Notes появилась в PostgreSQL.

## Этап 6. Создать DTO

Создать: - CreateNoteRequest - UpdateNoteRequest - NoteResponse

Изучить DTO и Validation Attributes.

## Этап 7. Создать сервис

Создать: - INotesService - NotesService

Реализовать: - Create - GetById - GetAll - Update - Delete

Изучить Dependency Injection.

## Этап 8. Сделать CRUD API

Эндпоинты:

-   POST /notes
-   GET /notes
-   GET /notes/{id}
-   PUT /notes/{id}
-   DELETE /notes/{id}

Проверить через Swagger.

## Этап 9. Валидация

Правила:

-   Title обязательный, максимум 100 символов
-   Content обязательный, максимум 5000 символов

Изучить: - Required - StringLength

## Этап 10. Архивация

Эндпоинты:

-   PATCH /notes/{id}/archive
-   PATCH /notes/{id}/unarchive

Поле IsArchived.

## Этап 11. Фильтрация

Примеры:

-   GET /notes?search=test
-   GET /notes?archived=true

Изучить Query Parameters и LINQ.

## Этап 12. Пагинация

Пример:

-   GET /notes?page=1&pageSize=10

Ответ:

``` json
{
  "items": [],
  "page": 1,
  "pageSize": 10,
  "total": 42
}
```

Изучить: - Skip() - Take() - CountAsync()

## Этап 13. Глобальная обработка ошибок

Сделать: - 404 - 400 - 500

Изучить Middleware и Exception Handling.

## Этап 14. Docker для API

Написать: - Dockerfile - docker-compose.yml

Запуск:

``` bash
docker compose up
```

Поднимаются: - Notes API - PostgreSQL

## Этап 15. Финальная версия

Должно работать:

-   Создание заметок
-   Редактирование
-   Удаление
-   Архивация
-   Поиск
-   Фильтрация
-   Пагинация
-   PostgreSQL
-   EF Core
-   Swagger
-   Docker

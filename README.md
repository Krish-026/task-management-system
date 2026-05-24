# Task Management System

A secure and scalable backend REST API built using Spring Boot for managing tasks with JWT authentication, role-based authorization, pagination, filtering, sorting, caching, and Swagger documentation.

---

# 🚀 Features

## Authentication & Security

- JWT Authentication
- Secure Login & Signup APIs
- Password Encryption using BCrypt
- Spring Security Integration
- Role-Based Access Control (RBAC)

## Roles

- ADMIN
- USER

## Task Management

- Create Tasks
- Update Tasks
- Delete Tasks
- Personalized Task Access
- Admin Task Management
- Filtering by Status
- Pagination
- Sorting

## API Features

- Global Exception Handling
- Request Validation
- Standard API Response Structure
- DTO + Mapper Pattern
- Dynamic & Class-Based Projection
- Swagger/OpenAPI Documentation
- Caching Support
- Layered Architecture
- Clean Separation of Concerns

---

# 🌐 Live API

## Swagger UI

```text
https://task-management-system-awh8.onrender.com/api/v1/swagger-ui/index.html
```

---

# 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JWT
- Maven
- Swagger/OpenAPI
- Lombok
- REST APIs
- Postman
- Git & GitHub

---

# 📁 Project Structure

```text
src/main/java/com/taskmanager/system
│
├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
├── security
├── exception
├── config
└── projection
```

---

# ⚙️ Setup Instructions

## 1. Clone Repository

```bash
git clone git@github.com:Krish-026/task-management-system.git
```

---

## 2. Open Project

Open the project in:

- IntelliJ IDEA
- VS Code
- Eclipse

---

## 3. Configure Database

Create MySQL database:

```sql
CREATE DATABASE task_db;
```

---

## 4. Create Application Properties

Copy:

```text
application-example.properties
```

Rename it to:

```text
application.properties
```

Then add your own:

- Database username
- Database password
- JWT secret

---

## 5. Run Application

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080/api/v1
```

---

# 🔐 Authentication APIs

## Signup

```http
POST /api/v1/auth/signup
```

## Login

```http
POST /api/v1/auth/login
```

After login, use JWT token in:

```text
Authorization: Bearer <token>
```

---

# 📚 Swagger Documentation

## Swagger UI

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

## API Docs

```text
http://localhost:8080/api/v1/v3/api-docs
```

---

# 📦 Sample API Response

```json
{
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Complete Backend",
    "status": "TODO"
  },
  "success": true,
  "timestamp": "2026-05-01T18:00:00"
}
```

---

# 🔑 RBAC Rules

| Action         | Allowed Roles                   |
|----------------|---------------------------------|
| Create Task    | Authenticated User             |
| Delete Task    | ADMIN                          |
| Update Task    | ADMIN / Creator / Assigned User |
| View All Tasks | ADMIN                          |
| View My Tasks  | Authenticated User             |

---

# 🚀 Future Improvements

- Docker Support
- Redis Caching
- Unit & Integration Testing
- CI/CD Pipeline
- AWS Deployment

---

# 👨‍💻 Author

Krishna Pratap Singh

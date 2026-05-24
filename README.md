# Task Management System

A secure backend REST API built using Spring Boot for managing tasks with JWT authentication, role-based authorization, pagination, sorting, filtering, caching, Swagger documentation, and clean API architecture.

---

# 🚀 Features

## Authentication & Security

* JWT Authentication
* Secure Login & Signup APIs
* Password Encryption using BCrypt
* Spring Security Integration
* Role-Based Access Control (RBAC)

## Roles

* ADMIN
* USER

## Task Management

* Create Task
* Update Task
* Delete Task
* Get My Tasks
* Get All Tasks
* Filtering by Status
* Pagination
* Sorting

## API Features

* Global Exception Handling
* Request Validation
* Standard API Response Structure
* DTO + Mapper Pattern
* Dynamic & Class-Based Projection
* Swagger/OpenAPI Documentation
* Caching Support

---

# ⚠️ Important Note

The **first registered user** in the system is automatically assigned the role:

```text
ADMIN
```

All users registered after the first user are assigned:

```text
USER
```

---

# 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* MySQL
* JWT
* Maven
* Swagger/OpenAPI
* Lombok

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

* IntelliJ IDEA
* VS Code
* Eclipse

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

* database username
* database password
* JWT secret

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

Swagger UI:

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

API Docs:

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
| -------------- | ------------------------------- |
| Create Task    | ADMIN                           |
| Delete Task    | ADMIN                           |
| Update Task    | ADMIN / Creator / Assigned User |
| View All Tasks | ADMIN                           |
| View My Tasks  | Authenticated User              |

---

# 🚀 Future Improvements

* OAuth2 Login
* Docker Support
* CI/CD Pipeline
* Redis Caching
* Email Notifications
* File Uploads
* Unit & Integration Testing
* Deployment on AWS/Render

---

# 👨‍💻 Author

Krishna Pratap Singh

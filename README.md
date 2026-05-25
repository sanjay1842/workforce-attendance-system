# Workforce Attendance & Overtime Management System

This project was built as part of the DeepThought backend engineering assignment.

I extended the provided Spring Boot project into a workforce attendance and overtime management backend with worker management, attendance lifecycle handling, overtime tracking, validation rules, and basic caching preparation.

The goal was to focus more on backend behavior, transactional workflows, and system structure instead of only implementing CRUD APIs.

---

# Base HRMS Reference

I referred to the existing Spring Boot HRMS-style structure from the provided repository because it already had a clean layered setup using controllers, services, repositories, and entity separation.

I kept that structure and expanded it instead of rebuilding everything from scratch.

---

# Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL (Supabase)
- Redis (prepared for caching layer)
- Maven
- Hibernate
- Lombok

---

# Features Implemented

## Worker Management
- Create workers
- Store designation and wage information
- Active/inactive worker tracking

## Site Management
- Create and manage work sites

## Attendance Workflow
- Clock-in system
- Clock-out system
- Prevention of duplicate active shifts
- Validation for active attendance records

## Overtime Workflow
- Automatic overtime calculation during clock-out
- Settlement status tracking
- Review flagging for suspicious long shifts

## Exception Handling
- Centralized exception handling
- Structured API error responses

## DTO-Based Responses
- Introduced DTOs to avoid exposing entity structure directly

## Caching Preparation
- Redis caching annotations added
- Cache eviction strategy prepared
- Temporarily disabled during local debugging when Redis server was unavailable

---

# Design Decisions & Notes

## Lazy Loading

I intentionally kept relationships lazy-loaded because attendance-related data can grow quickly. Eager loading would create unnecessary joins and larger responses.

---

## Transaction Handling

Clock-out updates both attendance records and overtime entries, so I wrapped those operations inside transactions to keep the data consistent.

---

## DTO Usage

Initially I returned entities directly from APIs, but later switched to DTOs after running into serialization and response-structure concerns.

---

## Redis

I prepared Redis caching for read-heavy endpoints like worker retrieval.

During development I temporarily disabled caching annotations while debugging locally because Redis infrastructure was not always running.

---

## Things I Would Improve With More Time

- JWT authentication
- Role-based access
- Full Redis setup with Docker
- Dockerized deployment
- Attendance reporting
- Analytics/dashboard layer
- CI/CD pipeline
- Swagger/OpenAPI documentation

---

# Database Structure

## Main Entities

- Worker
- Site
- AttendanceLog
- OvertimeEntry

## Relationships

- Worker → AttendanceLogs
- Site → AttendanceLogs
- AttendanceLog → OvertimeEntry

---

# API Endpoints

## Workers

### Create Worker

POST `/api/workers`

Example:

```json
{
  "fullName": "Arjun Mehta",
  "phoneNumber": "9123456781",
  "designation": "SUPERVISOR",
  "hourlyWage": 450
}
```

---

### Get All Workers

GET `/api/workers`

---

## Sites

### Create Site

POST `/api/sites`

Example:

```json
{
  "siteName": "Marathahalli Tech Park",
  "location": "Bengaluru"
}
```

---

### Get All Sites

GET `/api/sites`

---

## Attendance

### Clock In

POST `/api/attendance/clock-in`

Example:

```json
{
  "workerId": 1,
  "siteId": 1
}
```

---

### Clock Out

POST `/api/attendance/clock-out/{workerId}`

Example:

```text
POST /api/attendance/clock-out/1
```

---

# Running the Project

## 1. Clone Repository

```bash
git clone <your-repo-url>
```

---

## 2. Configure Supabase PostgreSQL

Update:

```properties
src/main/resources/application.properties
```

with:

```properties
spring.datasource.url=jdbc:postgresql://<host>:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<password>
```

---

## 3. Run Application

Windows:

```bash
.\mvnw.cmd spring-boot:run
```

---

# Redis Notes

Redis caching was prepared in the project but may require a local Redis instance or Docker container to enable fully.

While debugging locally, caching annotations were temporarily disabled to isolate infrastructure issues from API-level debugging.

---

# AI Usage

I used AI tools mainly for:
- debugging setup issues
- discussing backend design approaches
- refining validation logic
- understanding serialization and lazy-loading issues
- improving project structure incrementally

I still manually verified code behavior, tested APIs, debugged runtime issues, and adjusted implementation decisions during development.

---

# Commit Strategy

The project was developed incrementally with separate commits planned for:
- schema/entity setup
- attendance workflow
- overtime logic
- exception handling
- DTO integration
- Redis preparation
- API improvements

---

# Submission Notes

The project focuses mainly on backend architecture, workflow handling, and business-rule validation rather than frontend implementation.

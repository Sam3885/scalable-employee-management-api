# Scalable Employee Management API

A RESTful employee and project management backend built with Java 17, Spring Boot, Spring Data JPA/Hibernate and PostgreSQL.

## Features

- Department, employee and project CRUD APIs
- Employee-to-project assignments with duplicate-assignment protection
- DTO-based request/response handling (entities are not exposed directly)
- Bean Validation for request payloads
- Employee search using optional filters: name, department, salary range and designation
- Pagination and sorting for employee listing/search
- Transactional service methods
- Optimistic locking with JPA `@Version` on Employee and Project
- Database constraints and indexes for data integrity and common lookups
- Centralized exception handling with consistent JSON responses
- HTTP Basic authentication with ADMIN and EMPLOYEE roles

## Tech stack

- Java 17
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Spring Security
- Jakarta Bean Validation
- Maven

## Setup

1. Install JDK 17 and PostgreSQL.
2. Create a database named `employee_management`.
3. Configure environment variables (recommended):

   | Variable | Example | Purpose |
   |---|---|---|
   | `DB_URL` | `jdbc:postgresql://localhost:5432/employee_management` | JDBC URL |
   | `DB_USERNAME` | `postgres` | PostgreSQL user |
   | `DB_PASSWORD` | your local DB password | PostgreSQL password |
   | `ADMIN_PASSWORD` | a strong password | ADMIN login password |
   | `EMPLOYEE_PASSWORD` | a strong password | EMPLOYEE login password |

   For a quick local run, the application has development defaults for the database URL/user and HTTP Basic users. Set `DB_PASSWORD` to your actual PostgreSQL password. Change the default API passwords before sharing or deploying the application.

4. Import the project as an **Existing Maven Project** in Eclipse and allow Maven to update dependencies.
5. Run `EmployeeManagementApplication.java` as a Java application, or run `mvn spring-boot:run` from the project directory.
6. The API base URL is `http://localhost:8080`.

Hibernate uses `ddl-auto=update` for local development and creates/updates the schema automatically. For production, use a database migration tool such as Flyway or Liquibase instead.

## Authentication and authorization

Use **Basic Auth** in Hoppscotch/Postman (Authorization tab):

| Username | Default development password | Permissions |
|---|---|---|
| `admin` | `Admin@123` | GET, POST, PUT and DELETE API operations |
| `employee` | `Employee@123` | GET API operations only |

Set `ADMIN_PASSWORD` and `EMPLOYEE_PASSWORD` environment variables to override the development passwords. Do not use the defaults in production.

## API endpoints

All endpoints are under `/api` and require Basic Auth.

### Departments

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/departments` | Create department (ADMIN) |
| GET | `/api/departments` | List departments |

Example request:

```json
{
  "name": "IT",
  "description": "Information Technology Department"
}
```

### Employees

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create employee (ADMIN) |
| GET | `/api/employees` | List employees with pagination/sorting |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update employee (ADMIN) |
| DELETE | `/api/employees/{id}` | Delete employee if it has no project assignments (ADMIN) |
| GET | `/api/employees/search` | Search/filter employees |

Example employee request:

```json
{
  "name": "Amit Kumar",
  "email": "amit@example.com",
  "salary": 55000,
  "designation": "Backend Developer",
  "departmentId": 1
}
```

Pagination examples:

- `/api/employees?page=0&size=10`
- `/api/employees?page=0&size=5&sort=name,asc`

Search examples:

- `/api/employees/search?name=Amit`
- `/api/employees/search?departmentId=1&minSalary=30000&maxSalary=80000`
- `/api/employees/search?designation=Developer&page=0&size=10&sort=salary,desc`
- Filters can be combined. All are optional.

### Projects

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/projects` | Create project (ADMIN) |
| GET | `/api/projects` | List projects; optional `?status=ACTIVE` |
| GET | `/api/projects/{id}` | Get project by ID |
| PUT | `/api/projects/{id}` | Update project (ADMIN) |
| DELETE | `/api/projects/{id}` | Delete project if it has no assignments (ADMIN) |

Example project request:

```json
{
  "name": "Employee Management System",
  "description": "Enterprise employee management platform",
  "status": "ACTIVE",
  "budget": 600000,
  "startDate": "2026-11-05"
}
```

Allowed project statuses: `ACTIVE`, `COMPLETED`, `ON_HOLD`.

### Assignments

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/assignments` | Assign employee to project (ADMIN) |
| GET | `/api/assignments/employee/{employeeId}` | List assignments for an employee |
| GET | `/api/assignments/project/{projectId}` | List assignments for a project |

Example assignment request:

```json
{
  "employeeId": 1,
  "projectId": 1
}
```

An employee/project pair can only be assigned once. The database unique constraint protects this rule even if concurrent requests race.

## Error responses

Errors are returned as JSON with fields such as `status`, `error`, `message` and `path`. Validation failures also include a `details` object identifying invalid fields.

- `400 Bad Request`: invalid input or validation failure
- `401 Unauthorized`: missing/invalid Basic Auth credentials
- `403 Forbidden`: authenticated user lacks the required role
- `404 Not Found`: requested employee, department or project does not exist
- `409 Conflict`: duplicate data, blocked deletion or concurrent update conflict
- `500 Internal Server Error`: unexpected server-side failure

## Design notes

- Controllers handle HTTP concerns; services contain business rules; repositories handle persistence.
- DTOs prevent direct entity exposure and avoid bidirectional JSON serialization problems.
- `@Transactional` keeps multi-step business operations atomic.
- `@Version` enables optimistic locking for concurrent employee/project updates.
- Unique constraints protect employee email, department/project names where configured, and employee/project assignment pairs.
- Indexes support common employee and assignment lookups.

## Testing

Run the automated test suite with:

```bash
mvn test
```

The project includes a Spring application context smoke test. Also verify the API endpoints manually using Hoppscotch or Postman and a local PostgreSQL database.

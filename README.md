# High-Concurrency Ticketing System (Spring Boot)

---

## Table of Contents
- [About the Project](#about-the-project)
- [Packages](#packages)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Project Architecture](#project-architecture)
- [How to Run](#how-to-run)
- [API Endpoints](#api-endpoints)
- [Layers and Responsibilities](#layers-and-responsibilities)
- [Contact](#contact)

---

## About the Project
This project is a **high-concurrency ticket reservation system** built with **Spring Boot**.  
It is designed to handle **100k+ seats and 1M concurrent users**, ensuring **no double-selling of seats** under heavy load.  

It uses **pessimistic row locking (READ_COMMITTED)**, **short transactions**, and a **10-minute reservation hold** with a payment confirmation flow.  
The project also includes **AOP-based audit logging**, **exception handling**, and **concurrency tests**.

---

## Packages
```
com.example.ticketing
├── aspect/        # AOP for audit logging
├── config/        # Async, Jackson, Scheduler configurations
├── domain/        # Core enums (SeatStatus, PaymentStatus)
├── entity/        # JPA entities (Event, Seat, Reservation, Payment, AuditLog)
├── exception/     # GlobalExceptionHandler + custom exceptions
├── repository/    # Spring Data JPA repositories
├── scheduler/     # Scheduled jobs for expired reservations
├── service/       # Business logic (Reservation, Seat, Payment, AuditLog)
└── web/           # Controllers and DTOs
```

---

## Key Features
- **High-concurrency safe seat reservations** using pessimistic row locks  
- **10-minute reservation hold** before automatic expiry  
- **Payment confirmation flow** that atomically marks reservations as SOLD  
- **Audit logging with AOP**: every API call logged into DB  
- **Global exception handling** with meaningful error responses  
- **Scheduled cleanup job** for expired reservations  
- **Validation-ready DTOs** for input safety  
- **Concurrency and validation tests** included  

---

## Technologies Used
- **Java 17+**
- **Spring Boot 3.5.5**
- **Spring Data JPA** with pessimistic locking
- **Spring AOP** (audit logging)
- **Spring Validation**
- **Spring Scheduler** (cleanup job)
- **H2 Database** (dev/test)
- **JUnit 5 / Spring Boot Test**

---

## Project Architecture
- **Controller Layer (web/)** → Handles REST API requests and responses  
- **Service Layer (service/)** → Core business logic, transaction handling  
- **Repository Layer (repository/)** → Database access with Spring Data JPA  
- **Entity Layer (entity/)** → Maps database tables to Java objects  
- **Aspect Layer (aspect/)** → Cross-cutting concern: audit logging  
- **Scheduler (scheduler/)** → Periodically cleans expired reservations  
- **Exception Layer (exception/)** → Centralized error handling  

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.9+
- H2 (for development/testing)

### Steps
```bash
# Clone repository
git clone https://github.com/azizyilmaz/ticketing.git
cd ticketing

# Build project
mvn clean install

# Run with H2 (default)
mvn spring-boot:run
```

- Application runs at: `http://localhost:8080`   

---

## API Endpoints

### Reserve a Seat
```http
POST /api/reservations
```

### Confirm Payment
```http
POST /api/reservations/confirm
```

### Get Seat Status
```http
GET /api/seats/{id}
```

---

## Layers and Responsibilities

- **Controller** → API endpoints, request/response mapping  
- **Service** → Business logic, reservation/payment handling, transactions  
- **Repository** → Data persistence with pessimistic locks  
- **Entity** → Maps domain model to DB tables  
- **Aspect** → Logs API calls asynchronously for auditing  
- **Scheduler** → Periodic cleanup of expired reservations  
- **Exception** → Unified error responses across all APIs  

---

## Contact
**Author:** Aziz Yılmaz  
📧 Email: *azizxyilmaz@outlook.com*  
🔗 GitHub: [azizyilmaz](https://github.com/azizyilmaz)  


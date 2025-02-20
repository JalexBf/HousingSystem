# Property Rental Management System - Backend

## Overview
This is the **backend service** for a Property Rental Management System, built with **Spring Boot** and **Spring Security**. It provides APIs for **authentication, user roles, property listings, rental/viewing requests**, and **admin management**. 

---
## Log-in
- **Admin Panel**: Admin users log in through the **backend** at `http://localhost:8080`.
- **User Interface**: Owners and tenants log in through the **frontend** at `http://localhost:5173`.

Password for Admin user is currently: **'password123'** and database credentials can be found in the **application.properties** file.

---

## Features

### Authentication & Authorization
- **JWT-based authentication** (secured using Spring Security).
- **Role-based access control (RBAC)**:
  - **Admin**: Manage users & properties.
  - **Owner**: List & manage properties.
  - **Tenant**: Search, request viewings, and rent properties.
- **Protected API routes** with token validation.

### Property Listings & Management
- **Owners can:**
  - Add, and delete properties.
  - Approve or reject rental/viewing requests.
- **Tenants can:**
  - Browse available properties.
  - Submit **rental requests**.
  - Book **viewing appointments**.

### Search & Filtering
- Filter properties based on:
  - **Location**
  - **Category (Apartment, House, Room, etc.)**
  - **Price Range**
  - **Features & Size (m²)**

### API & Security
- **Spring Security with JWT authentication**.
- **CORS enabled** for frontend access.
- **Spring Boot REST APIs** following **MVC pattern**.

---

## Installation

### Clone the repository:
```sh
git clone https://github.com/JalexBf/HousingSystem
cd HousingSystem
```

### Configure Database & Environment Variables
- Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/housingdb
spring.datasource.username=root
spring.datasource.password=password
jwt.secret=secret-key
```

### Install Dependencies & Build the Project
```sh
mvn clean install
```

### Run the Server
```sh
mvn spring-boot:run
```
- The backend runs at **`http://localhost:8080`**.

---

## Project Structure
```
backend/
│── src/
│   ├── main/
│   │   ├── java/gr/hua/dit/ds/housingsystem/
│   │   │   ├── config/          # Security, JWT, CORS settings
│   │   │   ├── controllers/     # API controllers for authentication, properties, etc.
│   │   │   ├── entities/        # Data models (users, properties, requests)
│   │   │   ├── repositories/    # JPA repositories for DB queries
│   │   │   ├── services/        # Business logic implementations
│   │   │   │── DTO/             # Data Transfer Objects (DTOs) for API responses
│   │   ├── resources/            
│   │   │   ├── application.properties # Configuration file
│   │   │   ├── schema.sql       # Database schema
│── pom.xml                      # Maven dependencies
│── README.md                    # Documentation
```

---

## API Endpoints
### **Authentication**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/login` | Authenticate user and return JWT token |
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/signup` | Register a new user (same as `/auth/register`) |
| `POST` | `/login` | Authenticate user (same as `/auth/login`) |
| `GET`  | `/checkUsername` | Check if a username is available |
| `GET`  | `/checkEmail` | Check if an email is available |

### **User Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/users/me` | Get logged-in user details |
| `GET`  | `/users/{id}` | Get user by ID |
| `PUT`  | `/updateUser/{userId}` | Update user details |
| `GET`  | `/userDetailsPage/{userId}` | Get user profile page |
| `DELETE` | `/deleteUser/{userId}` | Delete a user (Admin only) |

### **Admin Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/admin/manage-users` | View/manage users (Admin only) |
| `GET`  | `/admin/manage-properties` | View/manage properties (Admin only) |
| `GET`  | `/approvedUsers` | Get all approved users |
| `GET`  | `/unapprovedUsers` | Get all unapproved users |
| `PUT`  | `/approveUser/{userId}` | Approve a user (Admin only) |
| `PUT`  | `/rejectUser/{userId}` | Reject a user (Admin only) |

### **Property Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/properties/` | Get all properties |
| `POST` | `/properties/` | Create a new property (Owner only) |
| `PUT`  | `/properties/{id}` | Update property details |
| `DELETE` | `/properties/{id}` | Delete a property (Owner only) |
| `GET`  | `/unapprovedProperties` | Get properties pending approval |
| `PUT`  | `/approveProperty/{propertyId}` | Approve a property listing |
| `PUT`  | `/rejectProperty/{propertyId}` | Reject a property listing |
| `GET`  | `/propertyDetails/{id}` | Get details of a property |
| `GET`  | `/area` | Get properties filtered by area |
| `GET`  | `/category/{category}` | Get properties filtered by category |
| `GET`  | `/search` | General search endpoint |
| `GET`  | `/searchByAreaAndDate` | Search properties by area and availability date |
| `GET`  | `/available` | Get all available properties |
| `GET`  | `/images/{filename}` | Retrieve an image file |

### **Rental & Viewing Requests**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/{tenantId}/add-rental-request` | Submit a rental request for a property |
| `POST` | `/{tenantId}/add-viewing-request` | Submit a viewing request for a property |
| `GET`  | `/{id}/availability-slots` | Get available viewing slots for a property |
| `GET`  | `/{id}/photos` | Get property photos |
| `PUT`  | `/{id}/status` | Update the status of a request (Admin/Owner) |

### **User ID Proof Endpoints**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/{userId}/idProof` | Retrieve ID proof for a user |
| `GET`  | `/{userId}/idProof/front` | Retrieve front side of ID proof |
| `GET`  | `/{userId}/idProof/back` | Retrieve back side of ID proof |

---

## Technologies Used
- **Spring Boot (Spring Security, Spring Data JPA)**
- **PostgreSQL**
- **JWT authentication**
- **Maven** (dependency management)
- **JUnit** (for testing)

---


## License
This project is licensed under the **MIT License**.

---

## Authors
- Jason Karafotias
- George Levantinos
- Christos Papilidis

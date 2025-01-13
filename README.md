
# Property Rental Management System - Backend

## Overview
This the backend application for a rental management platform that allows.


The backend is built using **Java Spring Boot** with **Spring Security** for authentication and **Spring Data JPA** for database management.

---

## Features

### Roles
1. **Administrator**:
   - Approves property listings and verifies tenant accounts.
   - Manages users (edit/delete).
2. **Owner**:
   - Lists properties, including details such as amenities, price, and availability slots.
   - Reviews and approves tenant rental/viewing requests.
3. **Tenant**:
   - Searches for properties using various filters.
   - Submits requests for property viewings and rental requests.

### Core Functionalities
- **Property Management**: Owners can register detailed property listings, which require admin approval before becoming publicly visible.
- **Search & Filtering**: Tenants can search for properties using filters such as price, area, and amenities.
- **Viewing & Rental Requests**: Tenants can request property viewings and submit rental requests. Owners can approve or reject these requests.
- **Notifications**: Users receive in-platform notifications about the progress of their requests and applications.
- **Authentication & Authorization**: The backend uses **JWT-based authentication** with role-based access control for secure operation.

---

## Technologies Used
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security (JWT)**
- **H2 Database** (for testing)
- **Maven** (for dependency management)

---

## Setup & Installation

### Prerequisites
- Java 17
- Maven 3.8+

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd housingsystem
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the application at:
   ```
   http://localhost:8080
   ```

---

## Database Configuration
By default, the application uses an **H2 in-memory database** for testing. You can switch to a persistent database (e.g., MySQL or PostgreSQL) by modifying the `application.properties` file:

```properties
# Example for MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/housingsystem
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```


---

## Testing
Unit tests are provided for key components:
- **Controllers**
- **Services**
- **Repositories**

To run the tests, use:
```bash
   mvn test
   ```

---

## API Endpoints

### **AppUserController**
Responsible for user management and handling identity proof files.

- `POST /users/{userId}/idProof`: Uploads ID proof files (front and back) for a user.
- `GET /users/{userId}/idProof/front`: Retrieves the front side of the user's ID.
- `GET /users/{userId}/idProof/back`: Retrieves the back side of the user's ID.

### **OwnerController**
Manages users with the role `OWNER`.

- `GET /api/owners`: Returns all users with the role `OWNER`.
- `GET /api/owners/{ownerId}`: Returns a specific user with the role `OWNER` based on their ID.

### **TenantController**
Manages users with the role `TENANT`.

- `GET /api/tenants`: Returns all users with the role `TENANT`.
- `GET /api/tenants/{tenantId}`: Returns a specific user with the role `TENANT` based on their ID.

### **AdminController**
Handles user and property management by an administrator.

- `PUT /admin/approveProperty/{propertyId}`: Approves a property by its ID.
- `DELETE /admin/rejectProperty/{propertyId}`: Rejects and deletes a property by its ID.
- `GET /admin/unapprovedProperties`: Returns all unapproved properties.
- `PUT /admin/approveUser/{userId}`: Approves a user by their ID.
- `PUT /admin/rejectUser/{userId}`: Rejects a user by their ID.
- `DELETE /admin/deleteUser/{userId}`: Deletes a user by their ID.
- `PUT /admin/updateUser/{userId}`: Updates user details by their ID.
- `GET /admin/unapprovedUsers`: Returns all unapproved users.

### **AuthController**
Responsible for user authentication and registration.

- `POST /api/auth/login`: Authenticates a user and returns a JWT token.

### **ViewController**
Renders HTML pages using Thymeleaf templates.

- `GET /`: Loads the home page (`index.html`).
- `GET /login`: Displays the login page (`login.html`).
- `GET /admin`: Shows the admin dashboard (`admin.html`).
- `GET /error`: Displays the error page (`error.html`).

### **NotificationController**
Manages notifications sent to users.

- `GET /api/notifications/{userId}`: Returns notifications for a user by their ID.
- `POST /api/notifications`: Creates a new notification for a user with a message.
- `DELETE /api/notifications/{id}`: Deletes a notification by its ID.
- `GET /api/notifications/username/{username}`: Returns the user ID based on the username.

### **PropertyController**
Handles property management.

- `POST /api/properties`: Creates or updates a property.
- `GET /api/properties/{id}`: Returns details of a property by its ID.
- `POST /api/properties/{id}/photos`: Adds photos to a property.
- `GET /api/properties/category/{category}`: Returns all properties in a specific category.
- `GET /api/properties/area`: Returns properties in a specific area.
- `GET /api/properties/owner/{ownerId}`: Returns properties owned by a specific owner.
- `POST /api/properties/{id}/approve`: Approves a property by its ID.
- `DELETE /api/properties/{id}`: Deletes a property by its ID.
- `GET /api/properties/search`: Searches properties based on various criteria such as category, price, location, and number of rooms.

### **RentalRequestController**
Manages rental requests.

- `GET /api/rental-requests`: Returns all rental requests.
- `GET /api/rental-requests/{id}`: Returns a specific rental request by its ID.
- `POST /api/rental-requests`: Creates a new rental request.
- `PUT /api/rental-requests/{id}/status`: Updates the status (PENDING, APPROVED, REJECTED) of a rental request.
- `DELETE /api/rental-requests/{id}`: Deletes a rental request by its ID.

### **ViewingRequestController**
Manages viewing requests.

- `GET /api/viewing-requests`: Returns all viewing requests.
- `GET /api/viewing-requests/{id}`: Returns a specific viewing request by its ID.
- `POST /api/viewing-requests`: Creates a new viewing request.
- `PUT /api/viewing-requests/{id}/status`: Updates the status (PENDING, APPROVED, REJECTED, CANCELLED) of a viewing request.
- `DELETE /api/viewing-requests/{id}`: Deletes a viewing request by its ID.

---


## Contributors
- Jason Karafotias
- George Levantinos
- Christos Papilidis

---

## License
This project is licensed under the MIT License.

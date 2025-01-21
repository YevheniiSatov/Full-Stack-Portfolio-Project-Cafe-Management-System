# Full Stack Cafe Management System

A comprehensive full-stack application designed to manage cafes, customer orders, courier operations, and geolocation-based services. This project demonstrates a strong backend architecture with a modern frontend interface.

---

## Features

### Backend
- **Spring Boot Framework**
- Role-based access control (Client, Cafe, Courier)
- REST API implementation for:
  - User registration and login
  - Order creation and management
  - Geolocation services
- **Database Management**:
  - Relational database schema for PostgreSQL/MySQL
  - Efficient data handling for orders, users, cafes, and menu items
- **Security**:
  - JWT-based authentication
  - Password hashing with BCrypt

### Frontend
- **React-based interface**
- Responsive design for clients, cafes, and couriers
- Real-time updates on order status
- Integration with geolocation services for cafe and courier functionality

---

## Prerequisites

### Tools and Technologies
- **Java 17+**
- **Spring Boot**
- **PostgreSQL/MySQL**
- **React** (Frontend framework)
- **Node.js and npm**
- External APIs:
  - Overpass API
  - Nominatim API

### Environment Variables
Ensure the following variables are configured:

```plaintext
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/cafeapp
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT Secret
JWT_SECRET=your_secret_key

# External API Endpoints
OVERPASS_API_URL=https://overpass-api.de/api/interpreter
NOMINATIM_API_URL=https://nominatim.openstreetmap.org
```

---

## Additional Setup Requirements

1. **Create Polygon Requests for Cafe Regions**:
   - Use the Overpass API to define polygon boundaries for cafe regions.
   - Ensure polygons are properly saved in the database to allow geolocation-based queries.

2. **Add Cafe and Menu Items**:
   - Populate the `cafes` table with the initial cafe data, including name, email, and associated regions.
   - Populate the `menu_items` table with dishes offered by each cafe.

3. **Modify Access File**:
   - Update `application.properties` or your environment file to reflect the correct database connection details and API URLs.

---

## Installation

### Backend
1. Clone the repository:
   ```bash
   git clone https://github.com/YevheniiSatov/full-stack-cafe-app.git
   cd full-stack-cafe-app/backend
   ```
2. Configure the `application.properties` file:
   ```properties
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update

   jwt.secret=${JWT_SECRET}
   overpass.api.url=${OVERPASS_API_URL}
   nominatim.api.url=${NOMINATIM_API_URL}
   ```
3. Build and run the backend:
   ```bash
   mvn clean install
   java -jar target/cafe-app-0.0.1-SNAPSHOT.jar
   ```

### Frontend
1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm start
   ```

---

## API Endpoints

### Authentication
- **POST /api/auth/register** - Register a new user
- **POST /api/auth/login** - Login an existing user

### Orders
- **POST /api/orders** - Place an order
- **GET /api/cafe/orders** - Get orders for a cafe
- **POST /api/cafe/orders/{id}/accept** - Accept an order by a cafe
- **GET /api/courier/orders** - View orders for couriers

### Geolocation
- **POST /api/geo/location** - Get nearest cafe by location
- **POST /api/geo/address** - Get nearest cafe by address

---

## Database Schema
- **Users**:
  - `id`, `email`, `password`, `role`, `name`
- **Orders**:
  - `id`, `client_email`, `status`, `latitude`, `longitude`
- **Order Items**:
  - `id`, `order_id`, `menu_item_id`, `quantity`
- **Cafes**:
  - `id`, `name`, `email`
- **Menu Items**:
  - `id`, `cafe_id`, `name`, `price`

---

## Contribution
Contributions are welcome! Please submit a pull request or create an issue for any improvements or fixes.

---

## Author
**Yevhenii Shatov**  
Backend and Full-Stack Developer  
[GitHub Profile](https://github.com/YevheniiSatov)

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

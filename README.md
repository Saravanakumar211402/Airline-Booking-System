# Airline Booking System Project

A Java-based airline booking system that manages passengers, flights, and bookings using DAO architecture and concurrency-safe operations.

## Features
- Create bookings with seat availability checks
- Save passenger and booking data via DAO
- Update flight seat counts in the database
- In-memory booking support for JUnit testing
- Exception handling for full flights

## Tech Stack
- Java 21
- Maven
- JDBC (PostgreSQL)
- Eclipse IDE
- DBeaver (for DB management)
- DAO Pattern (PassengerDAO, BookingDAO, FlightDAO)
- JUnit 5
- Mockito
- Byte Buddy (for mocking on Java 21)

## Database Setup
This project uses PostgreSQL for persistent storage. You can manage the database using DBeaver or any SQL client.

### Tables used:
- `passenger`
- `flight`
- `booking`

### JDBC Configuration:
Update your `BookingDAO`, `PassengerDAO`, and `FlightDAO` classes with your local DB credentials:
- String url = "jdbc:postgresql://localhost:5432/airline_db";
- String user = "your_username";
- String password = "your_password";

## Project Structure
```
airline-booking-system/
├── pom.xml               # Maven build configuration
├── README.md             # Project documentation
├── .gitignore            # Ignore build artifacts and IDE files
└── src/
    ├── main/
    │   └── java/
    │       └── airlinebookingapp/
    │           ├── dao/         → JDBC interfaces and implementations
    │           ├── exception/   → Custom exceptions (e.g., FlightFullException)
    │           ├── model/       → Domain classes (Passenger, Flight, Booking)
    │           ├── service/     → Business logic (BookingService, RevenueService)
    │           ├── thread/      → Concurrency handling (e.g., BookingThread)
    │           └── util/        → Utility classes (DBConnectionManager, etc.)
    └── test/
        └── java/
            └── airlinebookingapp/
                ├── stub/        → Fake DAO implementations for isolated testing
                └── test/        → JUnit test classes (BookingServiceTest, BookingServiceDaoTest)
```

## Future Improvements
- Add front-end interface (HTML/CSS/JS or React)
- Integrate with a real database (JDBC or JPA)
- Add concurrency-safe booking logic
- REST API layer using Spring Boot


## 🛠 Installation
```bash
git clone https://github.com/Saravanakumar211402/Airline-Booking-System.git




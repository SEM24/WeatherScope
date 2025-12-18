# WeatherScope Backend

A Spring Boot REST API application for fetching, storing, and analyzing weather data.

## Technology Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL / H2 Database
- Maven / Gradle
- Lombok
- Jakarta Validation

## Features

- Fetch current weather data for any city
- Store weather history in database
- Calculate average temperature over specified periods
- Retrieve weather trends and historical data
- RESTful API endpoints with validation

## Prerequisites

- JDK 17 or higher
- Maven 3.6+ or Gradle 7+
- PostgreSQL (or H2 for development)

## Installation

### 1. Clone the repository

```bash
git clone <repository-url>
cd weatherscope-backend
```

### 2. Configure database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/weatherscope
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

For H2 development database:

```properties
spring.datasource.url=jdbc:h2:mem:weatherdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
```

### 3. Configure external weather API

Add your weather API key to `application.properties`:

```properties
weather.api.key=your_api_key_here
weather.api.url=https://api.openweathermap.org/data/2.5/weather
```

### 4. Build the project

Using Maven:
```bash
./mvnw clean install
```

Using Gradle:
```bash
./gradlew build
```

### 5. Run the application

Using Maven:
```bash
./mvnw spring-boot:run
```

Using Gradle:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Get Current Weather

Fetch and save current weather data for a city.

```
GET /{city}
```

**Example:**
```bash
curl http://localhost:8080/London
```

**Response:**
```json
{
  "id": 1,
  "city": "london",
  "temperature": 15.5,
  "humidity": 65.0,
  "description": "partly cloudy",
  "timestamp": "2025-12-17T14:30:00"
}
```

### Get Average Weather

Calculate average temperature for a city over specified days.

```
GET /avg/{city}?days={days}
```

**Parameters:**
- `days` (required): Number of days (1-365)

**Example:**
```bash
curl http://localhost:8080/avg/London?days=7
```

**Response:**
```json
{
  "city": "london",
  "averageTemperature": 14.2,
  "days": 7
}
```

### Get Weather Trends

Retrieve weather data for the last N days.

```
GET /trends/{city}?days={days}
```

**Parameters:**
- `days` (required): Number of days (1-365)

**Example:**
```bash
curl http://localhost:8080/trends/London?days=30
```

**Response:**
```json
[
  {
    "id": 1,
    "city": "london",
    "temperature": 15.5,
    "humidity": 65.0,
    "description": "partly cloudy",
    "timestamp": "2025-12-17T14:30:00"
  },
  ...
]
```

### Get Weather History

Retrieve all historical weather data for a city.

```
GET /history/{city}
```

**Example:**
```bash
curl http://localhost:8080/history/London
```

**Response:**
```json
[
  {
    "id": 1,
    "city": "london",
    "temperature": 15.5,
    "humidity": 65.0,
    "description": "partly cloudy",
    "timestamp": "2025-12-17T14:30:00"
  },
  ...
]
```

## CORS Configuration

For frontend integration, CORS is enabled for `http://localhost:5173` by default.

To modify allowed origins, edit the CORS configuration:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## Project Structure

```
src/main/java/com/weatherscope/
├── controller/
│   └── WeatherController.java
├── service/
│   └── WeatherService.java
├── repository/
│   └── WeatherRepository.java
├── model/
│   ├── entity/
│   │   ├── WeatherData.java
│   │   └── dto/
│   │       └── AverageWeatherDto.java
└── config/
    └── WebConfig.java
```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK` - Successful request
- `400 Bad Request` - Invalid parameters (e.g., days out of range)
- `404 Not Found` - City not found or no data available
- `500 Internal Server Error` - Server error

## Testing

Run tests with:

```bash
./mvnw test
```

## Production Deployment

1. Build the JAR file:
```bash
./mvnw clean package -DskipTests
```

2. Run the JAR:
```bash
java -jar target/weatherscope-0.0.1-SNAPSHOT.jar
```

3. Configure production database and API keys via environment variables:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-host:5432/weatherscope
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=prod_password
export WEATHER_API_KEY=prod_api_key
```

## License

This project is licensed under the MIT License.
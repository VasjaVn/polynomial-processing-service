# Polynomial Processing Service

## Description
This service provides a RESTful API that simplifies polynomial expressions and stores queries in a
database. If the same expression has been processed before, the result retrieved from
the database cache instead of recalculating it.

## Application Architecture on High Level
![Application architecture on high level](/src/main/resources/static/images/arch_pps.png)

## Table of Contents
- 1.[Prerequisites](#1-prerequisites)
- 2.[Installation](#2-installation)
- 3.[Authentication](#3-authentication)
- 4.[Configuration](#4-configuration)
- 5.[Database Migrations with Flyway](#5-database-migrations-with-flyway)
- 6.[Running the Application](#6-running-the-application)
- 7.[Running the Application in a Docker Container](#7-running-the-application-in-a-docker-container)
- 8.[Usage](#8-usage)
- 9.[Testing](#9-testing)


## 1. Prerequisites
Before you begin, ensure you have the following software installed on your machine:
- **PostgreSQL**
- **Redis**
### Options for preparing local dev environment
#### Option 1: Manual Installation
    Download and install [PostgreSQL](https://www.postgresql.org/download/) and [Redis](https://redis.io/downloads/) on your machine
#### Option 2: Using Docker
1. **Install Docker**: Follow the instructions on the [Docker website](https://docs.docker.com/get-docker/).
2. **Run PostgreSQL**:
```bash
docker run --name postgres -e POSTGRES_PASSWORD=your_password -d -p 5432:5432 postgres
```
3. **Run Redis**:
```bash
docker run --name redis -d -p 6379:6379 redis
```
#### Option 3: Using Docker Compose
1. **Install Docker Compose:** Follow the instructions on the [Docker Compose website](https://docs.docker.com/compose/install/).
2. **Create a `docker-compose.yml` file** in your project directory:
```yaml
version: '3.3'
services:
  postgres:
    image: postgres
    environment:
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"

  redis:
    image: redis
    ports:
      - "6379:6379"
```
3. **Start the services:**
```bash
docker-compose up -d
```
4. **Stop and remove the containers defined in `docker-compose.yml`, use:**
```bash
docker-compose down
```
5. **Stop the containers without removing them, use:**
```bash
docker-compose stop
```
6. **To restart the containers, use:**
```bash
docker-compose up -d
```


## 2. Installation
```bash
git clone https://github.com/VasjaVn/polynomial-processing-service.git
cd polynomial-processing-service
mvn clean install
```

## 3. Authentication
This application uses `JSON Web Tokens (JWT)` for authentication.
### 3.1. Overview of JWT
**[JWT](https://jwt.io/)** is a compact, URL-safe means of representing claims to be transferred between two parties. The claims in a JWT are encoded as a JSON object that is used as the payload of a `JSON Web Signature (JWS)` structure or as the plaintext of a `JSON Web Encryption (JWE)` structure, enabling the claims to be digitally signed or integrity protected with a `message authentication code (MAC)` and/or `encrypted`.
### 3.2. Generate a `JWT Secret Key`
- Use this [online resource](https://jwtsecret.com/generate) for generation a `JWT Secret Key`.
### 3.3 Generate a `JWT Token`
- Use this [online resource](http://jwtbuilder.jamiekurtz.com/) for generation a `JWT Token`.
- For signing `JWT Token` use `JWT Secret Key`.
```plaintext
Authorization: Bearer `JWT Token`
```
### 3.4 Validate `JWT Token` 
- Use this [online resource](https://jwt.io/) for validation a `JWT Token`.


## 4. Configuration
### Environment variables:
```env
# Environment Variables for configuration PostgreSQL DataBase:
export DB_HOST='your_database_host' # default localhost 
export DB_PORT='your_database_port' # default 5432
export DB_NAME='your_database_name' # default postgres
export DB_USERNAME='your_database_username' # default postgres
export DB_PASSWORD='your_database_password' # default postgres

# Environment Variables for configuration Redis Cache:
export ENABLE_CACHE=true/false   # defualt false
export REDIS_HOST='your_redis_host' # default localhost
export REDIS_PORT='your_redis_port' # default 6379

# Environment Variable for configuration JWT Token Validator:
export JWT_SECRET_KEY='your_jwt_secret_key'
```


## 5. Database Migrations with Flyway

### Overview
Flyway is a database migration tool that helps you manage your database schema versioning and migrations in a reliable way.

### Setting Up Flyway
#### 1. Add and [configure](https://documentation.red-gate.com/fd/maven-goal-184127408.html) Flyway Maven Plugin to pom.xml:
```xml
<plugins>
    ...
    <plugin>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-maven-plugin</artifactId>
        <configuration>
            <url>jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/{DB_NAME:postgres}</url>
            <user>${DB_USERNAME:postgres}</user>
            <password>${DB_PASSWORD:postgres}</password>
            <cleanDisabled>false</cleanDisabled>
            <validateMigrationNaming>true</validateMigrationNaming>
            <locations>
                <location>filesystem:src/main/resources/db/migration</location>
            </locations>
        </configuration>
    </plugin>
    ...
</plugins>
```
#### 2 Create Migration Files
* Create a directory for your migration files, typically under src/main/resources/db/migration. Name your migration files using the Flyway **migration naming convention**.

### Migration Naming Conventions
Migration files should be named in the following format:
```php
V<version_number>__<description>.sql
```
For example:
```plaintext
V1__Create_polynomials_table.sql
```
### Running Migrations
You can run migrations using Maven commands. In your terminal, navigate to your project directory and run:
```bash
mvn flyway:migrate
```
### Other Flyway Commands
You can also use other Flyway commands through Maven:
* **Clean the database:**
```bash
mvn flyway:clean
```
* **Info about the migrations:**
```bash
mvn flyway:info
```
* **Undo the last migration:**
```bash
mvn flyway:undo
```

### Additional Resources
* [Flyway Documentation](https://brunomendola.github.io/flywaydb.org/documentation/)


## 6. Running the Application
### 6.1. Running from an IDE:
### 6.2. Running as a packaged application:
```bash
java -jar ./target/polynomial-processing-service-0.0.1-SNAPSHOT.jar
```
### 6.3. Using the Maven plugin:
```bash
mvn spring-boot:run
```


## 7. Running the Application in a Docker Container

### Overview
This application can be easily containerized using Docker, allowing for consistent deployment across different environments. Docker ensures that your application runs with all its dependencies in an isolated environment.

### Prerequisites
Before you begin, ensure you have the following installed on your machine:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/) (optional, for multi-container setups)

### Build the Docker Image
#### 1. Using [Jib Maven Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin):
- Include the Jib Maven Plugin in pom.xml
```xml
<plugins>
    ...
    <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <version>3.4.1</version>
        <configuration>
            <to>
                <image>polynomial-processing-service</image>
            </to>
        </configuration>
    </plugin>
    ...
</plugins>
```
- Run the following Maven command to build the Docker image locally:
```bash
mvn clean compile jib:dockerBuild
```
#### 2. Using Dockerfile
The `Dockerfile` is located in the root directory of the project.
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/polynomial-processing-service-0.0.1-SNAPSHOT.jar polynomial-processing-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "polynomial-processing-service.jar"]
```
To build the Docker image, run the following command in the root directory of the project:
```bash
docker build -t polynomial-processing-service .
```
### Running the Container
Once the image is built, you can run the container with the following command:
```bash
docker run -p 8080:8080 -e DB_HOST=127.0.0.1 -e DB_PORT=5432 -e DB_NAME=postgres \
       -e DB_USERNAME=postgres -e DB_PASSWORD=postgres \
       -e REDIS_HOST=127.0.0.1 -e REDIS_PORT=6379 \
       -e JWT_SECRET_KEY=... polynomial-processing-service
```
### Accessing the Application
You can access the application at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
### Cleaning Up
###### To stop the container, use:
```bash
docker stop <container_id>
```
###### To remove the container, use:
```bash
docker rm <container_id>
```
###### To remove the image, use:
```bash
docker rmi polynomial-processing-service
```


## 8. Usage
### 8.1. Using the Command Line Interface:
* [cURL](https://curl.se/)
###### To evaluate a polynomial, send a request to:
```bash
curl -X 'GET' \
  'http://localhost:8080/api/polynomials/evaluate?polynomial=x^2+2*x-1&value=1' \
  -H 'Authorization: Bearer {JWT_TOKEN}'
```
###### To delete a polynomial, send a request to:
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/polynomials?polynomial=x^2+2*x-1' \
  -H 'Authorization: Bearer {JWT_TOKEN}'
```
### 8.2. Using the Graphical User Interface:
* [Postman](https://www.postman.com/)
* [HTTPie](https://httpie.io/run)

### 8.3. Using Swagger UI:

#### 8.3.1. Accessing the Swagger UI
Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to access the interactive API documentation.

#### 8.3.2. API Endpoints
- **GET /api/polynomials/evaluate/?polynomial=string&value=integer**: Evaluate a polynomial.
- **DELETE /api/polynomials?polynomial=string**: Delete a polynomial.

#### 8.3.3. Authentication
To use the API, you must provide an JWT Token. Use the _"Authorize"_ button and add it in the _"Authorize"_ section of the Swagger UI.

#### 8.3.4. Using the Swagger Documentation
In the Swagger UI, you can:
- **Explore Endpoints**: Click on an endpoint to view details.
- **Try it Out**: Use the _"Try it out"_ button to test requests directly.

#### 8.3.5. Additional Resources
* [API Documentation](https://swagger.io/solutions/api-documentation/)
* [An overview of HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP/OverviewAn%20overview%20of%20HTTP)
* [Swagger Documentation](https://swagger.io/docs/)


## 9. Testing
```bash
mvn test
```
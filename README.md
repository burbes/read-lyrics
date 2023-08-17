# Lyrics Storage AWS Application

This repository contains the code for an AWS-powered application that stores song lyrics. The application leverages various AWS services such as Lambda, API Gateway, and RDS.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Deployment Steps](#deployment-steps)
3. [API Endpoints](#api-endpoints)
4. [Database Setup](#database-setup)
5. [Contributions](#contributions)
6. [License](#license)

## Prerequisites

- AWS CLI installed and configured with appropriate permissions.
- Java 8 or newer.
- Maven.
- An AWS account.

## Deployment Steps

1. **Set Up AWS Lambda with Spring Boot**

   To deploy a Spring Boot application on Lambda:

    ```bash
    mvn clean package
    ```

   This will generate a `.jar` file in the `target` directory. You'll need to upload this `.jar` file to your AWS Lambda function.

   Remember to set the Lambda handler to:
    ```bash
    helloworld.App::handleRequest
    ```

2. **API Gateway Configuration**

   After deploying your Lambda function, configure your API Gateway to route requests to the Lambda function.

3. **RDS Database Configuration**

   Set up an RDS instance using your preferred database (e.g., PostgreSQL, MySQL). Ensure that the security group attached to the RDS instance allows connections from your AWS Lambda function.

4. **Spring Boot Configuration**

   Adjust the `application.properties` file in your Spring Boot project to include the database connection details.

## API Endpoints

- **GET /lyrics/{key}**: Fetches lyrics based on the provided key.
- **POST /lyrics**: Saves new lyrics to the database. The request body should contain the lyrics data in a JSON format.

## Database Setup

In your RDS instance, use the following SQL schema:

```sql
CREATE TABLE lyrics (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    lyrics TEXT NOT NULL
);
```

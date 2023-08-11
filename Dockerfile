# Use a base Java image
FROM openjdk:11.0.5-slim-buster

# Set the working directory inside the container
WORKDIR /app

# Copy local code to the container image
COPY target/read-lyrics.jar /app/read-lyrics.jar

# Set the CMD to your application's main class
CMD ["java", "-jar", "/app/read-lyrics.jar"]

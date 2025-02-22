# Use JDK 21 as the base image
FROM eclipse-temurin:21

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project (excluding target/ and unnecessary files via .dockerignore)
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/hospital-0.0.1-SNAPSHOT.jar"]

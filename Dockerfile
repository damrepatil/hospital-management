# Use JDK 21 as the base image
FROM eclipse-temurin:21

# Set the working directory
WORKDIR /app

# Copy the whole project into the container
COPY . .

# Grant execute permissions to mvnw
RUN chmod +x mvnw

# Build the application inside the container
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the generated JAR file
CMD ["java", "-jar", "target/hospital-0.0.1-SNAPSHOT.jar"]




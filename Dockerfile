# Step 1: Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-alpine
# Step 2: Set the working directory inside the container
WORKDIR /app
# Step 3: Copy the JAR file from your project to container
COPY target/ecom-application.jar ecom-application.jar
# Step 4: Expose the port, that your application will run
EXPOSE 8080
# Step 5: Command to run the application
ENTRYPOINT ["java", "-jar", "/ecom-application.jar"]

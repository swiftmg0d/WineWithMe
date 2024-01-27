# Use an openjdk image as the base image
FROM openjdk:21-jdk-oracle

# Set the working directory in the image
WORKDIR /app

# Copy the jar file of the application to the image
COPY target/*.jar app.jar

# Expose port 9091 to the host
EXPOSE 5321

# Set the command to run the application when the container starts
CMD ["java", "-jar", "app.jar"]
# Use Java 17 (correct image)
FROM eclipse-temurin:17-jdk

# Copy jar file
COPY target/complaintsystem.jar app.jar

# Run app
ENTRYPOINT ["java","-jar","/app.jar"]
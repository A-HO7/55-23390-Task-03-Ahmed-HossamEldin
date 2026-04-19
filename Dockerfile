FROM eclipse-temurin:25.0.2_10-jdk

WORKDIR /app

COPY target/*.jar app.jar

# TODO: Replace with your full name, prefixed with Docker_. Format: Docker_FirstName_LastName
# Example: Docker_Mohamed_Ayman
ENV USER_NAME=Docker_Ahmed_HossamEldin

# TODO: Replace with your student ID, prefixed with Docker_. Format: Docker_XX_XXXXX
# Example: Docker_55_8078
ENV ID=Docker_55_23390

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:21
WORKDIR /app
COPY target/UserService-0.0.1-SNAPSHOT.jar /app/UserService-0.0.1-SNAPSHOT.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "UserService-0.0.1-SNAPSHOT.jar"]
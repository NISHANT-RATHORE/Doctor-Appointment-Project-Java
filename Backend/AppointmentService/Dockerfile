FROM openjdk:21
WORKDIR /app
COPY target/AppointmentService-0.0.1-SNAPSHOT.jar /app/AppointmentService-0.0.1-SNAPSHOT.jar
EXPOSE 9003
ENTRYPOINT ["java", "-jar", "AppointmentService-0.0.1-SNAPSHOT.jar"]
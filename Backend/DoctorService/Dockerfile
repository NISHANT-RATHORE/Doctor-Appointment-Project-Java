FROM openjdk:21
WORKDIR /app
COPY target/DoctorService-0.0.1-SNAPSHOT.jar /app/DoctorService-0.0.1-SNAPSHOT.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "DoctorService-0.0.1-SNAPSHOT.jar"]
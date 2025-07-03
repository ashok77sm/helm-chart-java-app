FROM maven

COPY target/demo-0.0.1-SNAPSHOT.jar /app/javaweb.jar

ENTRYPOINT ["java", "-jar", "/app/javaweb.jar"]

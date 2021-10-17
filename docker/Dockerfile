# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

# Copy war file
COPY api-0.0.1-SNAPSHOT.jar /api.war

# run the app
CMD ["/usr/bin/java", "-jar", "/api.war"]

# expose the port the api runs on in the container
EXPOSE 8080
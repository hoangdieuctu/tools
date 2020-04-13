FROM openjdk:8-jdk-alpine
COPY target/*.war app.war
ENTRYPOINT ["java","-jar","/app.war"]
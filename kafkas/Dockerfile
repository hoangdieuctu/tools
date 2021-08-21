FROM openjdk:8-jdk-alpine
COPY target/*.war app.war
ENTRYPOINT ["java","-Dkafka.envs=${ENV}","-jar","/app.war"]
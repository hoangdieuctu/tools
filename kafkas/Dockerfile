FROM adoptopenjdk/openjdk11:ubi
COPY target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-Dkafka.envs=${ENV}","-jar","/app.war"]
FROM openjdk:8-jdk-alpine
RUN mkdir -p /home/karros
ADD target/*.war /home/karros/app.war
ADD entrypoint.sh /home/karros/entrypoint.sh
RUN chmod +x /home/karros/entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/home/karros/entrypoint.sh"]
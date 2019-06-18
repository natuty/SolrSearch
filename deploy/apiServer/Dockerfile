FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dfile.encoding=UTF-8","-Duser.timezone=GMT+08","-jar","/app.jar","--spring.profiles.active=ns"]


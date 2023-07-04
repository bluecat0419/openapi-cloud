FROM java:8
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} openapi-email.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/openapi-email.jar"]

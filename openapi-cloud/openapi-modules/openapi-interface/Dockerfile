FROM java:8
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} openapi-interface.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/openapi-interface.jar"]

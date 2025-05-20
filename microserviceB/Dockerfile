FROM openjdk:21
COPY target/microserviceB-0.0.1-SNAPSHOT.jar /usr/src/myapp/
COPY opentelemetry-javaagent.jar /usr/src/myapp
WORKDIR /usr/src/myapp
CMD java -javaagent:opentelemetry-javaagent.jar -jar microserviceB-0.0.1-SNAPSHOT.jar
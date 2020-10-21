# tracing-demo
Show how to use Opentracing with Helidon/MicroProfile, Spring Boot and NodeJS

- microserviceA - port 8080 -(MicroProfile)
- microserviceB - port 8081 (Spring Boot)
- microserviceC - port 3000 (NodeJS)

Other services:
- MySQL - port 3306
- Kafka - port 9092
- Jaeger - ports 6831 and 16686

Just execute `./start-demo.sh`.

This will:
- compile java projects
- run npm install
- perform docker build
- execute docker-compose up

Then access: <http://localhost:8080/serial/Rafael>

Tracing information should be available at <http://localhost:16686/>

Slides are available at <http://bit.ly/cncfopentracing>

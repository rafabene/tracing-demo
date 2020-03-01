#!/bin/bash
(cd microserviceA; mvn clean package)
(cd microserviceB; mvn clean package)
(cd microserviceC; npm install)
docker-compose build
docker-compose up
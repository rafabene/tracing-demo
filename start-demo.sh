#!/bin/bash
(cd frontend; mvn clean package)
(cd microserviceB; mvn clean package)
docker-compose build
docker-compose up
#!/bin/bash
eval $(minikube -p demo docker-env)
(cd microserviceA; mvn clean package; docker build -t rafabene/microservicea -f src/main/docker/Dockerfile.helidon .)
(cd microserviceB; mvn clean package; docker build -t rafabene/microserviceb .)
(cd microserviceC; npm install; docker build -t rafabene/microservicec .)
kubectl create namespace tracing || echo
kubectl create namespace db || echo
kubectl label namespace tracing istio-injection=enabled  --overwrite
# Microservice A and Istio Gateway
kubectl apply -f microserviceA/kubernetes/deployment.yaml
kubectl apply -f microserviceA/kubernetes/service.yaml
kubectl apply -f microserviceA/kubernetes/gateway.yaml
# Microservice B and MySQL
kubectl apply -f microserviceB/kubernetes/deployment.yaml
kubectl apply -f microserviceB/kubernetes/service.yaml
kubectl apply -f microserviceB/kubernetes/mysql.yaml
# Microservice C
kubectl apply -f microserviceC/kubernetes/deployment.yaml
kubectl apply -f microserviceC/kubernetes/service.yaml
kubectl delete pods --all --grace-period 0 --force -n tracing
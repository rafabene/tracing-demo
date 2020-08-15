#!/bin/bash
export MS_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')

url="http://`minikube ip`:$MS_INGRESS_PORT"
echo "Ingress URLS: " 
echo "SERIAL:" $url/serial/Rafael
echo "PARALLEL:" $url/parallel/Rafael
echo "Chain:" $url/chain/Rafael


kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=jaeger -o jsonpath='{.items[0].metadata.name}') 15032:16686 &
sleep 1
DASHBOARD="http://localhost:15032/jaeger/search"
echo "JAEGER:" $DASHBOARD
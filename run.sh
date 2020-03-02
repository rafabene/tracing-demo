#!/bin/bash
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')

url="http://192.168.100.100:$INGRESS_PORT"
echo "Ingress URLS: " 
echo "SERIAL:" $url/serial/Rafael
echo "PARALLEL:" $url/parallel/Rafael
echo "Chain:" $url/chain/Rafael

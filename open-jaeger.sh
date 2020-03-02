#! /bin/bash
kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=jaeger -o jsonpath='{.items[0].metadata.name}') 15032:16686 &
sleep 1
DASHBOARD="http://localhost:15032/jaeger/search"
if [[ "$OSTYPE" == "linux-gnu" ]]; then
    gio open $DASHBOARD 
elif [[ "$OSTYPE" == "darwin"* ]]; then 
    open $DASHBOARD
fi

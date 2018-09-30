#!/usr/bin/env bash

docker stop conversion_proxy
docker rm conversion_proxy
docker rmi conversion_proxy/conversion_proxy:v1.0.0

docker build . -t conversion_proxy/conversion_proxy:v1.0.0

docker tag conversion_proxy/conversion_proxy:v1.0.0 localhost:5000/conversion_proxy





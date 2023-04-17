#!/bin/bash

docker container run -it --rm \
-v $(pwd):/data \
-v $(pwd)/m2:/root/.m2 \
-w /data/ \
maven:3.9-eclipse-temurin-17 \
mvn clean install

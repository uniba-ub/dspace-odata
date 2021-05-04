#!/bin/bash

docker container run -it --rm \
-v $(pwd):/data \
-v $(pwd)/m2:/root/.m2 \
-w /data/ \
maven:3.8.1-jdk-11 \
mvn clean install

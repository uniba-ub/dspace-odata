#!/bin/bash

docker container run -it --rm \
-v $(pwd):/data \
-v $(pwd)/m2:/root/.m2 \
-w /data/ \
maven:3.5.4-jdk-8 \
mvn clean install

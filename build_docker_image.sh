#!/bin/bash
mvn clean package -Pprod
docker build --tag orchest_test .
docker tag orchest_test:latest orchest_test:prod

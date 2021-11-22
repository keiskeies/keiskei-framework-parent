#/bin/bash
set -e
mvn clean package -Dmaven.test.skip=true -U
version=$(date "+%Y-%m-%d-%H-%M")
docker build -f ./Dockerfile -t IMAGE_NAME:v2.0.0-$version .
docker push IMAGE_NAME:v2.0.0-$version
kubectl -n NAME_SPACE set image deployment/DEPPLOY_NAME DEPLOY_NAME=IMAGE_NAME:v2.0.0-$version
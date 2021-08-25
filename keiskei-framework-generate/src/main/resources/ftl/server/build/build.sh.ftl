<#noparse>
#/bin/bash
set -e
mvn clean package -Dmaven.test.skip=true -U
version=$(date "+%Y-%m-%d-%H-%M")
docker build -f ./Dockerfile -t IMAGENAME:v1.0.0-$version .
docker push IMAGE_NAME:v1.0.0-$version
kubectl -n NAME_SPACE set image deployment/DEPLOY_NAME IMAGE_NAME:v1.0.0-$version
</#noparse>
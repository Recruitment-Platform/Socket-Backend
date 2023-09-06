#!/bin/bash

RUNNING_APPLICATION=$(docker ps | grep blue)
DEFAULT_CONF="/etc/nginx/conf.d/socketing.site.conf"

if [ -n "$RUNNING_APPLICATION" ]; then
  echo "green 배포 시작"
  IMAGE_ID=$(docker inspect --format '{{.Image}}' green)
  docker rm green
  docker rmi $IMAGE_ID
  docker run --name green -d -p 8081:8080 $AWS_ECR_URL:$SHORT_SHA

  sleep 10
  while [ 1 == 1 ]; do
    echo "green health check...."
    RESPONSE=$(curl -s http://localhost:8081/actuator/health)
    UP_COUNT=$(echo $RESPONSE | grep 'UP' | wc -l)
    if [ $UP_COUNT -ge 1 ]; then
      echo "> Green Health Check 성공"
      break
    fi
    sleep 3
  done

  echo "reload nginx"
  sudo sed -i 's/8080/8081/g' $DEFAULT_CONF
  sudo nginx -s reload
  docker kill --signal=SIGTERM blue
else
  echo "blue 배포 시작..."
  IMAGE_ID=$(docker inspect --format '{{.Image}}' green)
  docker rm blue
  docker rmi $IMAGE_ID
  docker run --name blue -d -p 8080:8080 $AWS_ECR_URL:$SHORT_SHA

  sleep 10
  while [ 1 == 1 ]; do
    echo "blue health check...."
    RESPONSE=$(curl -s http://localhost:8080/actuator/health)
    UP_COUNT=$(echo $RESPONSE | grep 'UP' | wc -l)
    if [ $UP_COUNT -ge 1 ]; then
      echo "> Blue Health Check 성공"
      break
    fi
    sleep 3
  done

  echo "reload nginx"
  sudo sed -i 's/8081/8080/g' $DEFAULT_CONF
  sudo nginx -s reload
  docker kill --signal=SIGTERM green
fi

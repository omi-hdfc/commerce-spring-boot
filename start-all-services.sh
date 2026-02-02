#!/bin/bash

# Kill any existing services
echo "Stopping existing services..."
pkill -9 -f "ConfigserverApplication|OrderApplication|ProductApplication|UserApplication" 2>/dev/null
lsof -ti:8888,8080,8081,8083 | xargs kill -9 2>/dev/null
sleep 3

# Start Config Server
echo "Starting Config Server on port 8888..."
cd /Users/OMKAR_S/ecommerce-course/configserver
nohup mvn spring-boot:run > logs/configserver.log 2>&1 &
sleep 15

# Start Order Service
echo "Starting Order Service on port 8083..."
cd /Users/OMKAR_S/ecommerce-course/order
DB_USER=admin DB_PASSWORD=admin nohup mvn spring-boot:run > logs/order.log 2>&1 &
sleep 10

# Start Product Service
echo "Starting Product Service on port 8081..."
cd /Users/OMKAR_S/ecommerce-course/product
DB_USER=admin DB_PASSWORD=admin nohup mvn spring-boot:run > logs/product.log 2>&1 &
sleep 10

# Start User Service
echo "Starting User Service on port 8080..."
cd /Users/OMKAR_S/ecommerce-course/user
DB_USER=admin DB_PASSWORD=admin nohup mvn spring-boot:run > logs/user.log 2>&1 &

echo "All services starting... Check logs in each service's logs/ directory"
echo "Wait 30-60 seconds for all services to fully start"

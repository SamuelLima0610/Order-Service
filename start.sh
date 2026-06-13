#!/bin/bash

set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "==> Starting infrastructure..."
docker compose -f "$ROOT_DIR/docker-compose.yml" up -d

echo "==> Waiting for Kafka to be ready..."
sleep 10

echo "==> Building shared events module..."
cd "$ROOT_DIR/shared/events" && mvn install -q -DskipTests

echo "==> Building order-service..."
cd "$ROOT_DIR/services/order-service" && mvn clean package -q -DskipTests

echo "==> Building payment-service..."
cd "$ROOT_DIR/services/payment-service" && mvn clean package -q -DskipTests

mkdir -p "$ROOT_DIR/logs"

echo "==> Starting order-service (port 8081)..."
java -jar "$ROOT_DIR/services/order-service/target/order-service-0.0.1-SNAPSHOT.jar" \
  > "$ROOT_DIR/logs/order-service.log" 2>&1 &
ORDER_PID=$!

echo "==> Starting payment-service (port 8082)..."
java -jar "$ROOT_DIR/services/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar" \
  > "$ROOT_DIR/logs/payment-service.log" 2>&1 &
PAYMENT_PID=$!

echo ""
echo "Services started!"
echo "  order-service   PID=$ORDER_PID  -> logs/order-service.log"
echo "  payment-service PID=$PAYMENT_PID -> logs/payment-service.log"
echo ""
echo "To stop all services, run: ./stop.sh"

echo "$ORDER_PID" > "$ROOT_DIR/logs/order-service.pid"
echo "$PAYMENT_PID" > "$ROOT_DIR/logs/payment-service.pid"

wait

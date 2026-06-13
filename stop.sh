#!/bin/bash

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOGS_DIR="$ROOT_DIR/logs"

stop_service() {
  local name=$1
  local pidfile="$LOGS_DIR/$name.pid"

  if [ -f "$pidfile" ]; then
    PID=$(cat "$pidfile")
    if kill -0 "$PID" 2>/dev/null; then
      echo "==> Stopping $name (PID=$PID)..."
      kill "$PID"
    else
      echo "==> $name is not running."
    fi
    rm -f "$pidfile"
  else
    echo "==> No PID file found for $name."
  fi
}

stop_service "order-service"
stop_service "payment-service"

echo "==> Stopping infrastructure..."
docker compose -f "$ROOT_DIR/docker-compose.yml" down

echo "All services stopped."

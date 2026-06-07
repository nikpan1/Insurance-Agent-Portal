#!/bin/bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRONTEND_DIR="$ROOT_DIR/frontend"
BACKEND_DIR="$ROOT_DIR/backend"

echo "Starting infrastructure (PostgreSQL, MongoDB)..."
if command -v docker-compose >/dev/null 2>&1; then
  docker-compose up -d
else
  docker compose up -d
fi

echo "Building backend..."
pushd "$BACKEND_DIR" >/dev/null
mvn clean package -DskipTests
BACKEND_JAR=""
for jar_file in target/*.jar; do
  if [[ "$jar_file" != *"original"* ]]; then
    BACKEND_JAR="$jar_file"
    break
  fi
done

if [[ -z "$BACKEND_JAR" ]]; then
  echo "No backend JAR found in $BACKEND_DIR/target"
  exit 1
fi
popd >/dev/null

echo "Building frontend..."
pushd "$FRONTEND_DIR" >/dev/null
npm install
npm run build
popd >/dev/null

cleanup() {
  echo "Stopping frontend and backend..."
  kill "$BACKEND_PID" "$FRONTEND_PID" 2>/dev/null || true
}

trap cleanup EXIT INT TERM

echo "Starting backend..."
pushd "$BACKEND_DIR" >/dev/null
java -jar "$BACKEND_JAR" &
BACKEND_PID=$!
popd >/dev/null

echo "Starting frontend..."
pushd "$FRONTEND_DIR" >/dev/null
npm run start -- --host 0.0.0.0 &
FRONTEND_PID=$!
popd >/dev/null

echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo "Application is running. Press Ctrl+C to stop both processes."

wait "$BACKEND_PID" "$FRONTEND_PID"

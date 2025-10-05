#!/bin/bash
set -euo pipefail

echo "============================="
echo " 🚀 Starting AI-SS Deployment"
echo "============================="

if [ ! -f .env ]; then
  echo "❌ .env not found"; exit 1
fi
set -a; source .env; set +a

echo "🔐 ECR login..."
aws ecr get-login-password --region "${AWS_REGION}" | \
  docker login --username AWS --password-stdin "${APP_ECR_REPO%/*}"

echo "🧹 Down old containers..."
docker compose down || true

echo "📦 Pull ${APP_ECR_REPO}:${IMAGE_TAG}"
docker compose pull

echo "🚀 Up..."
docker compose up -d --remove-orphans

echo "⏱  Health..."
sleep 5
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo "🧽 Prune old images..."
docker image prune -f --filter "until=168h" || true

echo "============================="
echo " ✅ All done. Server is live!"
echo "============================="

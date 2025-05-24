#!/bin/bash

# Google Cloud Run デプロイスクリプト (Java 24対応)

set -e

# 設定
PROJECT_ID="your-project-id"
SERVICE_NAME="springboot-demo"
REGION="asia-northeast1"
IMAGE_NAME="gcr.io/${PROJECT_ID}/${SERVICE_NAME}"

echo "🚀 Java 24 Spring Boot アプリをGoogle Cloud Runにデプロイ中..."

# Dockerイメージビルド
echo "📦 Dockerイメージをビルド中..."
docker build -t ${IMAGE_NAME}:latest .

# Google Cloud認証確認
echo "🔐 Google Cloud認証を確認中..."
gcloud auth application-default print-access-token > /dev/null

# イメージをGoogle Container Registryにプッシュ
echo "⬆️  イメージをプッシュ中..."
docker push ${IMAGE_NAME}:latest

# Cloud Runにデプロイ
echo "🌟 Cloud Runにデプロイ中..."
gcloud run deploy ${SERVICE_NAME} \
  --image ${IMAGE_NAME}:latest \
  --platform managed \
  --region ${REGION} \
  --allow-unauthenticated \
  --memory 1Gi \
  --cpu 1 \
  --concurrency 80 \
  --max-instances 10 \
  --port 8080 \
  --set-env-vars="SPRING_PROFILES_ACTIVE=production" \
  --project ${PROJECT_ID}

echo "✅ デプロイ完了！"
echo "🌐 URL: https://${SERVICE_NAME}-${REGION}-${PROJECT_ID}.a.run.app" 
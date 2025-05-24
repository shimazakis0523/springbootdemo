# Java 24対応マルチステージビルド
FROM eclipse-temurin:24-jdk-alpine AS builder

# 作業ディレクトリ設定
WORKDIR /app

# Gradleラッパーとビルドファイルをコピー
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# ソースコードをコピー
COPY src/ src/

# 実行権限を付与してビルド実行
RUN chmod +x gradlew && \
    ./gradlew clean build -x test --no-daemon

# 実行用の軽量イメージ
FROM eclipse-temurin:24-jre-alpine

# メタデータ
LABEL maintainer="shimazaki@example.com"
LABEL description="Spring Boot 3.5.0 with Java 24 Demo API"

# 非rootユーザーでセキュリティ向上
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 作業ディレクトリとアプリディレクトリ作成
WORKDIR /app
RUN mkdir -p /app/logs && chown -R appuser:appgroup /app

# ビルド成果物をコピー
COPY --from=builder --chown=appuser:appgroup /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# ユーザー切り替え
USER appuser

# ヘルスチェック
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# ポート公開
EXPOSE 8080

# JVMオプション最適化
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# アプリケーション起動
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 
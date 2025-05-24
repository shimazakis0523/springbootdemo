# Spring Boot Demo with OpenAPI Generator

[![Java CI with Gradle](https://github.com/shimazakis0523/springbootdemo/actions/workflows/test.yml/badge.svg)](https://github.com/shimazakis0523/springbootdemo/actions/workflows/test.yml)

OpenAPI Generator を使用した Spring Boot サンプルプロジェクトです。

## 📋 機能

- **OpenAPI 3.0** 仕様に基づくAPI定義
- **Spring Boot 3.x** + Java 17
- **自動コード生成** (OpenAPI Generator)
- **JUnit 5** による包括的なテスト
- **GitHub Actions** による自動テスト
- **Jacoco** テストカバレッジレポート

## 🚀 使用方法

### 前提条件
- Java 17以上
- Git

### セットアップ

1. **リポジトリクローン**
   ```bash
   git clone https://github.com/shimazakis0523/springbootdemo.git
   cd springbootdemo
   ```

2. **プロジェクトビルド**
   ```bash
   ./gradlew build
   ```

3. **テスト実行**
   ```bash
   ./gradlew test
   ```

4. **アプリケーション起動**
   ```bash
   ./gradlew bootRun
   ```

### API仕様確認

アプリケーション起動後、以下にアクセス：
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🔧 開発

### OpenAPI定義の変更

1. `src/main/resources/openapi/api.yaml` を編集
2. コード再生成: `./gradlew openApiGenerate`
3. ビルド: `./gradlew build`

### テストカバレッジ確認

```bash
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

## 📁 プロジェクト構造

```
├── src/
│   ├── main/
│   │   ├── java/com/shimazaki/demo/
│   │   │   ├── controller/     # REST Controllers
│   │   │   ├── service/        # Business Logic
│   │   │   └── config/         # 設定クラス
│   │   └── resources/
│   │       └── openapi/api.yaml # OpenAPI定義
│   └── test/                   # テストコード
├── .github/workflows/          # GitHub Actions
└── build.gradle                # ビルド設定
```

## 🔄 CI/CD

GitHub Actions により以下が自動実行されます：

- **プッシュ時**: 全テスト実行
- **プルリクエスト時**: テスト実行 + レポート
- **テストカバレッジ**: Jacoco レポート生成

## 🛠️ 技術スタック

- **Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.14.1
- **Testing**: JUnit 5, Mockito
- **Documentation**: OpenAPI 3.0, Swagger UI
- **Code Generation**: OpenAPI Generator

## 📊 テスト状況

現在のテストカバレッジと結果は[GitHub Actions](https://github.com/shimazakis0523/springbootdemo/actions)で確認できます。 
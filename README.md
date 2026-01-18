# Ecommerce ESUN

一個簡單的電子商務系統範例專案，包含 **後端 Spring Boot** 與 **前端 Vue.js**。

---

## 專案架構

```
├─ backend
│  ├─ pom.xml
│  └─ src
│     └─ main
│        ├─ java
│        │  └─ com
│        │     └─ esun
│        │        └─ ecommerce
│        │           ├─ Application.java              # Spring Boot 主程式入口
│        │           ├─ config
│        │           │  └─ CorsConfig.java            # 跨域設定
│        │           ├─ controller
│        │           │  ├─ ProductController.java
│        │           │  └─ OrderController.java
│        │           ├─ dto
│        │           │  ├─ OrderRequestDTO.java
│        │           │  └─ OrderItemDTO.java
│        │           ├─ entity
│        │           │  ├─ Product.java
│        │           │  ├─ Order.java
│        │           │  └─ OrderDetail.java
│        │           ├─ repository
│        │           │  ├─ ProductRepository.java
│        │           │  ├─ OrderRepository.java
│        │           │  └─ OrderDetailRepository.java
│        │           └─ service
│        │              ├─ ProductService.java
│        │              └─ OrderService.java
│        └─ resources
│           └─ application.properties
└─ frontend
   ├─ package.json
   └─ src
      ├─ main.js
      ├─ App.vue
      └─ components
         ├─ ProductList.vue
         ├─ Cart.vue
         └─ OrderSummary.vue
```

---

## 專案簡介

Ecommerce ESUN 是一個教學與範例用途的電子商務系統，目標是示範前後端分離的專案架構：

* 後端：Spring Boot 提供 REST API、資料持久化（使用 Spring Data JPA 與 PostgreSQL）。
* 前端：Vue 3（使用 Vite）作為單頁應用，呼叫後端 API 並呈現商品、購物車和訂單功能。

---

## 後端（Backend）

後端使用 **Spring Boot + Spring Data JPA + PostgreSQL**。

### 環境需求

* Java 17
* Maven
* PostgreSQL（資料庫名稱：`ecommerce_esun`）


### 配置 `application.properties`

請在 `backend/src/main/resources/application.properties` 設定：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_esun
spring.datasource.username=你的Postgres帳號
spring.datasource.password=你的Postgres密碼
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

server.port=8080
```

> 如果要生產環境部署，請改用 environment variables 或 `application-prod.properties`，並把 `spring.jpa.hibernate.ddl-auto=update` 改為 `validate` 或移除。

### 常用指令：

在專案根目錄的 `backend` 資料夾下執行：

```bash
# 下載相依並啟動
mvn spring-boot:run

# 或先打包再跑 jar
mvn clean package
java -jar target/ecommerce-1.0.0.jar
```

* 預設伺服器會啟動在 `http://localhost:8080`。

---

## 前端（Frontend）

前端使用 **Vue.js 3 + Vite** 建立簡單電子商務 UI。

### 環境需求

* Node.js 16+（或官方建議版本）
* npm 或 yarn

### 安裝依賴

在 `frontend` 資料夾下執行：

```bash
npm install
# 或
# yarn
```

### 啟動前端

```bash
npm run dev
# 或 (舊專案可能為)
# npm run serve
```

* Vite 預設會啟動在 `http://localhost:5173`（請以 CLI 顯示為主）。

### 組件說明

* `ProductList.vue`：商品列表並提供加入購物車功能。
* `Cart.vue`：購物車管理（顯示、修改數量、移除）。
* `OrderSummary.vue`：結帳與建立訂單的 UI。

## 本地開發流程（同時啟動前後端）

1. 確認 PostgreSQL 啟動，並建立 `ecommerce_esun` 資料庫。
2. 在 `backend` 設定好 `application.properties` 並啟動後端：`mvn spring-boot:run`。
3. 在 `frontend` 執行 `npm install`，再 `npm run dev`。
4. 開啟瀏覽器訪問前端（預設 `http://localhost:5173`），前端會呼叫 `http://localhost:8080/api` 的後端 API。

---

## 測試（建議）

* 後端：使用 JUnit + Spring Boot Test 撰寫單元與整合測試。
* 前端：使用 Vitest / Jest + Vue Testing Library 測試元件與 API 呼叫。

---

## 注意事項

* 確認 PostgreSQL 資料庫 `ecommerce_esun` 已存在。
* 後端連線資訊要正確（帳號密碼、資料庫名稱）。
* 前後端需同時運行才能測試完整功能。
* 如果有 CORS 問題，請確認 `CorsConfig` 或 Spring 的 `@CrossOrigin` 設定。

# 🛍️ Shop

Shop — это учебное full-stack приложение для e-commerce, в котором реализованы регистрация, каталог товаров, поиск и корзина.

## 🚀 Features

- ✅ Регистрация и вход с JWT (токенами доступа)
- 🔍 Поиск товаров с фильтрами и фасетами
- 🛒 Каталог и корзина товаров
- 📦 Кеширование с Redis
- 🧪 Тесты на бэкенде
- 🔐 Redis для токенов подтверждения и корзины

---

## ⚙️ Технологии

- **Backend:** Spring Boot
- **Frontend:** Angular
- **База данных:** MongoDB
- **Кэширование и хранилище состояния:** Redis
- **Контейнеризация:** Docker & Docker Compose

---

## 🧑‍💻 Как запустить проект

### 📦 Backend (Spring Boot)

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/HuNGuM/Shop-backend.git
   cd Shop
   ```
2. Убедитесь, что вы настроили подключение к базе данных и Redis в `src/main/resources/application.properties`. Пример:
    ```properties
    spring.data.mongodb.uri=mongodb://mongo:27017/storeDB
    spring.redis.host=redis
    spring.redis.port=6379
    ```
3. Путь к фронтенду в `docker-compose.yml`

    Найдите в `docker-compose.yml` секцию `frontend:` и убедитесь, что пути выставлены корректно под вашу локальную систему. Например:

    ```yaml
    frontend:
     build:
    context: ../mydir/ShopFrontend/Shop-frontend
    dockerfile: Dockerfile
    ports:
    - "4200:4200"
    volumes:
    - ../mydir/ShopFrontend/Shop-frontend:/app





    ПРИМЕЧАНИЕ> Замените `../mydir/ShopFrontend/Shop-frontend` на фактический путь к вашему фронтенду.

2. Соберите проект:
   ```bash
   mvn clean package
   ```

3. Запустите через Docker Compose:
   ```bash
   docker-compose up --build
   ```

🚀 После этого приложение будет доступно по адресу: `http://localhost:8080`

---

### 🌐 Frontend (Angular)

1. Склонируйте фронт:
   ```bash
   git clone https://github.com/HuNGuM/Shop-frontend.git
   cd Shop-frontend
   ```

2. Установите зависимости:
   ```bash
   npm install --legacy-peer-deps
   ```

3. Запустите сервер разработки:
   ```bash
   ng serve
   ```

🌍 Фронтенд будет доступен по адресу: `http://localhost:4200`

---

## 🐳 Альтернатива: Запуск всего через Docker Compose

Если настроен `docker-compose.yml` с сервисами:
- `app` (Spring Boot)
- `mongo`
- `redis`
- `frontend`

Можно просто запустить:

```bash
docker-compose up --build
```

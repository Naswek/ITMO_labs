# Интерфейс LabWork

React + Vite. Исходники интерфейса находятся в `frontend/src`, серверный код — в `backend`.

```bash
npm install
npm run dev
```

В режиме разработки Vite проксирует `/api` на `http://localhost:8080/labworks/api`. Если сервер слушает другой адрес или контекст, задайте `VITE_BACKEND_ORIGIN` и `VITE_BACKEND_CONTEXT`.

Для развёртывания выполните `npm run build`, затем `cd ../backend && ./gradlew :app:war`. Содержимое `frontend/dist` попадёт в WAR, а интерфейс и REST API будут доступны с одного адреса.

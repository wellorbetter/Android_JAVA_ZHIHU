# Backend (Phase 3 MVP)

这是一个零依赖（JDK内置 `HttpServer`）的后端 MVP，用来打通 Android 端联调闭环。

## 启动
```bash
cd backend
./run.sh
```

服务地址：`http://localhost:8080`

## 接口
- `POST /v1/auth/login`
- `POST /v1/auth/logout`
- `GET /v1/feed/recommend`（需要 Bearer token）
- `GET /v1/feed/following`（需要 Bearer token）
- `GET /v1/topics`（需要 Bearer token）

## 快速验证
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/v1/auth/login | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')
curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/v1/feed/recommend
curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/v1/topics
```


## 爬虫种子
先运行：
```bash
python3 crawler/fetch_programmer_seed.py
```

后端会优先读取 `crawler/data/programmer_seed.json`。

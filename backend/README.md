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
- `GET /v1/feed/hot`（需要 Bearer token）
- `GET /v1/feed/idea`（需要 Bearer token）
- `POST /v1/content/posts`（需要 Bearer token）
- `POST /v1/content/{id}/like`（需要 Bearer token）
- `POST /v1/content/{id}/comments`（需要 Bearer token）
- `GET /v1/content/{id}/comments?cursor=&limit=`（需要 Bearer token）
- `POST /v1/content/{id}/comments/delete-latest`（需要 Bearer token，仅删除本人最新评论）
- `GET /v1/topics`（需要 Bearer token）
- `GET /v1/system/health`（无需登录）

## 快速验证
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/v1/auth/login | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')
curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/v1/feed/recommend
curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/v1/topics
curl -s -X POST -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"KMP 生产化实践","summary":"从共享层到多端发布","topicName":"Android"}' \
  http://localhost:8080/v1/content/posts
```


## 爬虫种子
先运行：
```bash
python3 crawler/fetch_programmer_seed.py
```

后端会优先读取 `crawler/data/programmer_seed.json`。

## 内容持久化
- 用户发布内容会持久化到：`backend/data/content_store.tsv`

## 生产化数据库迁移
- 迁移脚本目录：`backend/db/migrations`
- 迁移说明：`backend/db/README.md`

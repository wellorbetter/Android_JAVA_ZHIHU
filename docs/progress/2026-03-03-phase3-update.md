# 进展播报（Phase 3 - 后端联调闭环）

## 今日完成
1. 新增 `backend` 模块（JDK HttpServer 版本）并可独立运行。
2. 实现 Auth/Feed/Topic 五个接口：
   - `POST /v1/auth/login`
   - `POST /v1/auth/logout`
   - `GET /v1/feed/recommend`
   - `GET /v1/feed/following`
   - `GET /v1/topics`
3. 加入 token 鉴权拦截（Bearer Token），未授权返回 `40101/AUTH_REQUIRED`。
4. 增加 `backend/run.sh` 一键编译启动脚本与 README 联调说明。

## 闭环状态
- Android 客户端：已具备 Retrofit + Token 注入骨架
- 后端服务：已提供可运行 MVP API
- 联调路径：`10.0.2.2:8080` 已可直接对接

## 下一阶段（Phase 4）
1. 后端切换到 Spring Boot + MySQL + Redis
2. 接入真实登录（验证码/密码）和 Refresh Token
3. Feed/Topic 改为数据库驱动

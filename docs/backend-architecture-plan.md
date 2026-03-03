# 仿知乎项目重构蓝图（跨端 + Private + 可演进）

## 1. 目标与原则

### 业务目标
1. 一套后端服务，支持 Android / Web / iOS。
2. 默认私密（Private by Default）：未授权用户仅可访问白名单资源。
3. 支持程序员垂类分区（后端、前端、AI、Android 等）。

### 工程原则
- **先模块化单体，再微服务演进**（避免过早复杂化）。
- **领域分层**：API / Application / Domain / Infrastructure。
- **契约优先**：先定义 API Contract，再让各端并行开发。

---

## 2. 后端建议架构

### 2.1 逻辑分层
- `api`：Controller、Request/Response DTO、鉴权注解
- `application`：UseCase、事务编排、权限校验
- `domain`：聚合根、领域服务、仓储接口
- `infrastructure`：MySQL、Redis、MQ、搜索引擎、对象存储实现

### 2.2 模块划分
- `user-service`：用户、资料、关注关系
- `content-service`：问题、回答、评论、话题、标签
- `interaction-service`：点赞、收藏、浏览、举报
- `feed-service`：推荐流、关注流、热榜
- `auth-service`：JWT、RefreshToken、设备会话
- `ops-service`：审核、风控、审计日志

> 第一阶段建议作为模块化单体部署，二阶段按压测热点拆分 `feed/auth/interaction`。

---

## 3. 数据模型（核心）
- 用户域：`user`, `user_profile`, `user_device`, `follow_relation`
- 内容域：`question`, `answer`, `comment`, `topic`, `tag`, `content_tag_relation`
- 互动域：`like_record`, `favorite_record`, `view_record`, `report_record`
- 安全域：`session_token`, `risk_event`, `audit_log`

### 程序员分区模型
- `topic`（一级分区）：Backend/Frontend/AI/Android/Database/SystemDesign
- `tag`（二级标签）：Java/Spring/Redis/Kafka/Compose/LeetCode
- `content_topic_relation` + `content_tag_relation` 用于召回和筛选

---

## 4. 跨端 API 契约（最小闭环）

### Auth
- `POST /v1/auth/login`（手机号验证码 / 密码）
- `POST /v1/auth/refresh`
- `POST /v1/auth/logout`

### Feed
- `GET /v1/feed/recommend?cursor=...`
- `GET /v1/feed/following?cursor=...`

### Content
- `POST /v1/questions`
- `GET /v1/questions/{id}`
- `POST /v1/questions/{id}/answers`
- `POST /v1/content/{id}/like`

### Taxonomy
- `GET /v1/topics`
- `GET /v1/topics/{id}/feed?cursor=...`

### Private 策略
- 受限接口统一返回 `401 + businessCode=AUTH_REQUIRED`
- 客户端收到后跳转登录或个人页

---

## 5. Android 端重构策略（已落地 + 下一步）

### 已落地
- 会话仓储接口 `SessionRepository` + `SharedPrefsSessionRepository`
- `MainNavigationGuard` 独立处理私密模式拦截逻辑
- `MainActivity` 仅负责绑定 UI 与导航决策（降低耦合）

### 下一步
1. 接入 Retrofit + OkHttp 拦截器（自动附带 Token）
2. 引入 Repository + UseCase 到首页推荐流
3. 从静态数据替换为分页接口数据（cursor based）

---

## 6. 推荐系统（从简单到高级）
1. V1 规则：热度 + 新鲜度 + 分区匹配
2. V2 轻量排序：特征打分（点击率、停留时长、互动率）
3. V3 模型化：召回（向量/协同过滤）+ 排序（LTR）

---

## 7. 4 周执行计划
- Week1：Auth + Question/Answer/Comment + Like + 私密权限中间件
- Week2：Topic/Tag 分区 + 搜索 + 收藏 + 关注流
- Week3：推荐流（规则版）+ 通知 + 审计日志
- Week4：风控（限频/反刷/敏感词）+ 指标看板 + 灰度发布

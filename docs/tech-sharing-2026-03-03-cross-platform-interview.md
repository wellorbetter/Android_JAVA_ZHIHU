# 面试级技术分享：仿知乎知识社区的跨端工程化升级

## 1. 项目背景
目标是把一个 Android 静态页面项目，升级为可演进的跨端产品雏形：
- 客户端：Android（当前），iOS/Web 可平滑接入
- 服务端：统一 API 契约，承载认证、推荐流、分区筛选
- 数据侧：通过爬虫提供冷启动内容

## 2. 为什么这样设计

### 2.1 先后端契约，再多端接入
跨端协作最怕“字段漂移”。因此先锁定：
- `GET /v1/topics`
- `GET /v1/feed/recommend?topic=...`
并统一返回结构 `code/message/data`，降低 Android/iOS/Web 端对接成本。

### 2.2 冷启动问题优先解决
社区产品上线初期常见问题是“无内容”。
本次通过 `crawler/fetch_programmer_seed.py` + `CrawlerSeedService` 打通了种子数据读取，再在 `FeedService` 里进行 topic 分类和合并输出，确保第一天就有可浏览内容。

### 2.3 Android 侧做解耦
将网络调用从 Fragment 中抽离到 `HomeFeedRepository`，并将 UI 调整为：
- 横向分类栏（All/Android/iOS/Web/Backend/AI）
- 纵向推荐流
这样后续接入分页、缓存、埋点更自然。

## 3. 本次关键改造

### 3.1 后端
- `ProtectedHandler` 支持按请求读取 query 参数
- `FeedService` 支持 `topic` 筛选并融合 crawler 冷启动数据
- `TopicService` 扩展 iOS/Web 分类

### 3.2 Android
- 新增 `TopicFilterAdapter`（横向分类）
- 新增 `HomeFeedRepository`（网络访问抽象）
- `RecommendedFragment` 重构为单页双列表结构
- 推荐项展示增加 topic 信息

### 3.3 Agent 能力建设（全局）
新增 skill：`cross-platform-fullstack-architect`，并安装安全/测试/CI 辅助 skill，形成个人开发的长期能力底座。

## 4. 工程收益
1. 业务闭环：分类筛选从 UI 点击直达后端真实过滤。
2. 架构可扩展：新增分类只需后端 topic 数据 + 前端渲染，无需重写页面结构。
3. 跨端准备：API 契约已具备 iOS/Web 接入基础。
4. 交付标准化：有严格 code review 与技术分享模板，可复用到后续迭代。

## 5. 取舍与技术债
1. 为保持零依赖后端，JSON 解析暂用正则，鲁棒性一般。
2. Android 仍是 Fragment + callback，未迁移到 ViewModel 状态管理。
3. feed 还未实现 cursor 真分页与本地缓存。

## 6. 下一步路线
1. 后端引入标准 JSON 解析与持久化（MySQL/SQLite + Redis 缓存）。
2. Android 迁移 MVVM（ViewModel + Repository + UiState）。
3. iOS 端按同一契约补齐首页分类流。
4. 引入 OpenAPI 文档生成，确保跨端协作一致性。

## 7. 可讲的面试亮点
- 从“静态页面”到“跨端契约优先”的系统化升级路径
- 冷启动数据方案（爬虫种子）与推荐流融合
- 结构化 code review 与工程化交付习惯
- 独立开发者如何构建长期可复用的 AI Agent 能力体系

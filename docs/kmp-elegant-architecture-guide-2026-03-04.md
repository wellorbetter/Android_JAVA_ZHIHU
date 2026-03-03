# KMP 优雅架构落地指南（针对当前项目）

## 为什么是这套方案
目标是“一套核心代码，多端跑”，但不牺牲体验和可维护性。
因此选择：
- 共享：业务规则、数据模型、接口访问、错误处理
- 原生：Android/iOS UI 渲染与平台交互

这比“全 UI 共享”更稳、更易维护，也更适合独立开发者持续迭代。

## 当前落地结构
- `shared`（KMP）
  - `HomeModels`：Topic/FeedItem 领域模型
  - `HomeRepository`：统一数据抽象
  - `GetTopicsUseCase` / `GetRecommendFeedUseCase`
  - `KtorHomeDataSource`：共享网络访问实现
  - `HomeSdk`：对各端暴露回调式调用接口
- `app`（Android）
  - `KmpHomeGateway`：Android 侧桥接 shared
  - `RecommendedFragment`：只处理 UI 绑定和交互

## 优雅点在哪里
1. UI 不管网络细节，只管状态更新。
2. 业务逻辑不在 Fragment 里，便于测试和迁移。
3. iOS 后续可直接复用 shared 的 UseCase/DataSource。
4. topic 新增只影响后端数据和少量映射，不会牵动页面架构。

## 你可以重点学习的工程思想
1. Contract First：先锁接口，再做多端。
2. Layered Design：UI / UseCase / Repository / DataSource 分层。
3. Platform Adapter：平台差异放在工厂层（AndroidHomeSdkFactory / iOS factory）。
4. Progressive Refactor：先迁一条核心链路，再逐步替换其他页面。

## 下一步建议
1. 把 `following/hot` 也迁进 shared，形成统一 FeedUseCase。
2. 引入 `sealed class AppError` 统一错误语义。
3. Android 迁移到 ViewModel + UiState（MVI/MVVM）。
4. 为 shared 增加 commonTest 单元测试。

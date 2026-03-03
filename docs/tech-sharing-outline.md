# 技术分享：从仿知乎静态页面到跨端可演进系统

## 1. 背景
当前项目从 Android 静态页面起步，目标升级为：
- Private by Default
- 程序员分区内容社区
- 跨端统一能力（Android/Web/iOS）

## 2. 为什么先重构客户端
- 先把“状态、导航、会话”从页面里抽出来，避免后面接后端时全面返工。
- 通过 `SessionRepository + NavigationGuard`，建立可替换的业务边界。

## 3. 架构演进路径
1. Android Demo（静态）
2. Android 工程化重构（已进行）
3. 后端 MVP（Auth + Content + Feed）
4. 数据分区（Topic/Tag）
5. 跨端契约（OpenAPI）
6. 推荐系统分阶段演进

## 4. 爬虫是否必要
- 非必要，但可做冷启动。
- 优先 UGC 闭环，再考虑采集。
- 采集必须满足合法合规、来源标注和审核机制。

## 5. 实战建议（4周）
- Week1: Auth + 发布/回答/点赞
- Week2: Topic/Tag + 搜索
- Week3: 推荐流 + 通知
- Week4: 审核风控 + 监控告警 + 灰度发布

## 6. 可复用经验
- 不要直接把 “临时功能” 绑死在 Activity/Fragment。
- 先分层再做功能，长期效率更高。
- 用可替换接口封装核心策略（会话、鉴权、推荐）。

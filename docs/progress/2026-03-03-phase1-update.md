# 进展播报（Phase 1 - 架构闭环起步）

## 今日完成
1. 导航权限决策模型从 UI 绑定中剥离到纯业务对象：
   - `MainDestination`
   - `MainNavigationGuard`
2. 增加 `MainTabMapper` 负责 Android menuId -> 业务目标映射。
3. 为 `MainNavigationGuard` 增加 4 条单元测试，覆盖：
   - 私密模式关闭
   - 私密模式开启+未登录（拦截）
   - 私密模式开启+未登录访问个人页（放行）
   - 私密模式开启+已登录（放行）

## 当前闭环状态
- Android 端：导航权限闭环 ✅
- 自动化测试：权限策略单测 ✅
- 后端服务：未开始（下一阶段）
- 爬虫数据管线：未开始（后置）

## 下阶段目标（Phase 2）
1. 新建 backend 模块（Spring Boot）
2. 落地 Auth + Topic + Feed 基础接口
3. Android 接入 Retrofit，替换模拟登录

# 严格 Code Review 报告（2026-03-03）

## 范围
- Android 首页推荐流改造
- 后端 topic 筛选与 crawler 冷启动接入
- 全局 skill 配置

## Findings（按严重度）

### High
1. `backend/src/main/java/com/example/zhihu/backend/service/FeedService.java`
- 问题：Crawler JSON 解析采用正则，结构变化时有脆弱性。
- 影响：上游字段顺序/格式变动可能导致冷启动数据丢失。
- 建议：下一阶段引入标准 JSON 解析（Jackson/Gson）并加入解析失败指标。

### Medium
2. `app/src/main/java/com/example/android_java/Pages/HomeFragment.java`
- 问题：一级 Tab（想法/关注/推荐/热榜）当前复用同一个 `RecommendedFragment` 逻辑。
- 影响：语义上与真实业务不一致，后续扩展会增加认知负担。
- 建议：拆分为独立 feed 类型（recommend/following/hot/idea）并统一复用一个 `FeedListFragment` 基类。

3. `app/src/main/java/com/example/android_java/Pages/Home/RecommendedFragment.java`
- 问题：UI 层直接感知 Repository callback，尚未引入 ViewModel 状态容器。
- 影响：生命周期切换时恢复能力较弱，状态复用困难。
- 建议：下一步迁移到 MVVM（ViewModel + LiveData/StateFlow）。

### Low
4. `app/src/main/res/layout/fragment_home_recommend_items.xml`
- 问题：文案与颜色存在硬编码。
- 影响：国际化和主题切换能力不足。
- 建议：提取到 `strings.xml` / `colors.xml`。

## 已验证项
- 后端可编译通过（javac）
- 后端接口烟测通过：login / topics / feed recommend(topic)
- Android Gradle 测试未完成（网络下载 Gradle 包超时）

## 结论
改造方向正确，已形成“爬虫冷启动 + 后端筛选 + 客户端横向分类”的闭环。当前主要风险是 JSON 解析鲁棒性和客户端状态层抽象深度。

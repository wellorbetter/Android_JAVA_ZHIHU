# Code Review 报告（Android_JAVA_ZHIHU）

## Review 范围
- `MainActivity`
- `MainVPAdapter`
- `Core/Session/*`
- `Core/Navigation/*`
- `PersonalFragment`
- `fragment_personal.xml`

## 总评
- 本轮重构方向正确：把会话逻辑、导航策略与 UI 层做了初步解耦。
- 代码质量从“Demo式堆叠”提升到“可维护的应用骨架”。

## 优点
1. **职责分离**：`MainNavigationGuard` 独立拦截策略，减少 Activity 复杂度。
2. **可演进**：`SessionRepository` 接口化，便于切换本地/远端会话实现。
3. **健壮性提升**：`MainVPAdapter` 不再返回 null Fragment。

## 主要问题与建议

### P1（高优先级）
1. **缺少单元测试**
   - 建议为 `MainNavigationGuard` 增加纯 Java 单测，覆盖：
     - 私密模式开/关
     - 登录/未登录
     - 各 Tab 的访问决策

2. **UI 与状态同步仍是手工驱动**
   - 当前靠 `renderSessionStatus()` 手动刷新。
   - 建议升级到 MVVM（ViewModel + LiveData/StateFlow）自动驱动 UI。

3. **依赖注入仍为轻量容器**
   - `AppContainer` 可用，但规模扩大后维护成本上升。
   - 建议中期接入 Hilt/Koin。

### P2（中优先级）
1. `MainTab.fromMenuId()` 对未知 id 回退 PERSONAL，建议记录告警日志。
2. `Toast` 文案可以统一通过事件总线或 UI Event 分发，减少 Activity 中硬编码。
3. `fragment_personal.xml` 建议增加可访问性属性与暗色主题适配验证。

## 结论
- 本轮可作为“架构基线 PR”合入。
- 合入后应立即补 `NavigationGuard` 单测与 Retrofit 接入，进入真实后端联调阶段。

# 开源 Agent/Skills 配置调研（2026-03-03）

## 1. 调研范围
- OpenAI 官方 skills 体系（curated/system）
- 社区技能集合（awesome-llm-skills）
- 黑客马拉松评审维度（可复现性、稳定性、演示完整度）

## 2. 核心结论
1. 冠军/高分项目的共同点不是“花哨 Prompt”，而是**流程可复用 + 工程可验证**。
2. 最稳妥的全局配置是：
- 一个领域主 skill（你的 Android+iOS+Backend）
- 若干通用能力 skill（安全、测试、CI 修复）
3. Skill 设计必须遵循“渐进加载”：
- 元数据触发
- SKILL.md 只放流程
- 大量细节沉淀到 references/scripts

## 3. 你的全局配置落地
已新增：
- `~/.codex/skills/cross-platform-fullstack-architect`
- `~/.codex/skills/security-best-practices`
- `~/.codex/skills/playwright`
- `~/.codex/skills/gh-fix-ci`

## 4. 对独立开发者的建议模板
- 主 skill：产品架构与交付流程（你已配置）
- 质量 skill：安全/代码审查
- 自动化 skill：E2E/CI
- 输出规范：每次交付都包含 API 契约、测试证据、风险清单、技术分享

## 5. 参考链接
- OpenAI skills 仓库：https://github.com/openai/skills
- awesome-llm-skills：https://github.com/alyrazik/awesome-llm-skills
- Hackathon 评审示例（Devpost 指南）：https://help.devpost.com/article/124-judging-on-devpost

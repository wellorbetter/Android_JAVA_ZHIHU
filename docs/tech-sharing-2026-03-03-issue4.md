# 技术分享（第 4 期）：跨端 + 爬虫 + 后端如何形成闭环

## 闭环定义
1. 爬虫生产数据种子
2. 后端读取并服务化输出
3. Android/Web 统一通过 API 消费

## 关键点
- 协议一致优先于技术栈一致。
- 先做可运行 MVP，再逐步替换为重型工程化方案。
- 爬虫仅做冷启动，不替代 UGC 主链路。

## 本期成果
- Crawler: `programmer_seed.json`
- Backend: `/v1/crawler/seed`
- Web: 可视化调试页面

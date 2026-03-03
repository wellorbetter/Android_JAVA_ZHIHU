# Crawler (程序员分区冷启动)

## 说明
通过 RSS 抓取技术社区公开信息，生成本地种子数据，用于程序员分区冷启动。

## 运行
```bash
python3 crawler/fetch_programmer_seed.py
```

输出：`crawler/data/programmer_seed.json`

## 数据字段（schemaVersion=2）
- `source`
- `title`
- `url`
- `publishedAt`
- `summary`
- `topicName`（Android/iOS/Web/Backend/AI）
- `authorName`

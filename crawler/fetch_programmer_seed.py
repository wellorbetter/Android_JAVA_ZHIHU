#!/usr/bin/env python3
import json
import os
import time
import urllib.request
import urllib.parse
import xml.etree.ElementTree as ET

RSS_SOURCES = [
    ("v2ex-tech", "https://www.v2ex.com/feed/tab/tech.xml"),
    ("v2ex-create", "https://www.v2ex.com/feed/tab/create.xml"),
]

WANANDROID_API = "https://www.wanandroid.com/article/list/0/json"

FALLBACK_ITEMS = [
    {
        "source": "fallback",
        "title": "Java 微服务如何做链路追踪？",
        "url": "https://example.com/java-tracing",
        "publishedAt": "",
        "summary": "OpenTelemetry + SkyWalking 的落地路径",
        "topicName": "Backend",
        "authorName": "fallback"
    },
    {
        "source": "fallback",
        "title": "Android 启动优化 Checklist",
        "url": "https://example.com/android-startup",
        "publishedAt": "",
        "summary": "冷启动指标、主线程治理、IO 预加载",
        "topicName": "Android",
        "authorName": "fallback"
    }
]


def parse_rss(xml_content: bytes, source: str):
    root = ET.fromstring(xml_content)
    channel = root.find("channel")
    if channel is None:
        return []

    items = []
    for item in channel.findall("item")[:20]:
        title = (item.findtext("title") or "").strip()
        link = (item.findtext("link") or "").strip()
        pub_date = (item.findtext("pubDate") or "").strip()
        desc = (item.findtext("description") or "").strip()
        if not title or not link:
            continue
        items.append({
            "source": source,
            "title": title,
            "url": link,
            "publishedAt": pub_date,
            "summary": desc[:160],
            "topicName": infer_topic(title, desc),
            "authorName": source
        })
    return items


def parse_wanandroid(payload: bytes):
    try:
        body = json.loads(payload.decode("utf-8", errors="ignore"))
        data = body.get("data", {})
        article_list = data.get("datas", [])
        items = []
        for item in article_list[:20]:
            title = (item.get("title") or "").strip()
            link = (item.get("link") or "").strip()
            summary = (item.get("desc") or "").strip()
            if not title or not link:
                continue
            items.append({
                "source": "wanandroid",
                "title": title,
                "url": link,
                "publishedAt": str(item.get("niceDate") or ""),
                "summary": summary[:160],
                "topicName": infer_topic(title, summary),
                "authorName": (item.get("author") or item.get("shareUser") or "wanandroid")
            })
        return items
    except Exception:
        return []


def fetch_source(name, url):
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        with urllib.request.urlopen(req, timeout=20) as resp:
            data = resp.read()
        return parse_rss(data, name)
    except Exception:
        return []


def fetch_wanandroid():
    try:
        req = urllib.request.Request(WANANDROID_API, headers={"User-Agent": "Mozilla/5.0"})
        with urllib.request.urlopen(req, timeout=20) as resp:
            data = resp.read()
        return parse_wanandroid(data)
    except Exception:
        return []


def infer_topic(title: str, summary: str):
    text = (title + " " + summary).lower()
    if any(k in text for k in ("android", "kotlin", "jetpack", "compose")):
        return "Android"
    if any(k in text for k in ("ios", "swift", "swiftui")):
        return "iOS"
    if any(k in text for k in ("react", "vue", "javascript", "frontend", "web", "css", "html")):
        return "Web"
    if any(k in text for k in ("ai", "llm", "gpt", "agent", "rag", "模型")):
        return "AI"
    return "Backend"


def normalize_items(items):
    dedup = {}
    for item in items:
        title = (item.get("title") or "").strip()
        url = (item.get("url") or "").strip()
        if not title or not url:
            continue
        key = urllib.parse.urlparse(url).path or title
        dedup[key] = {
            "source": (item.get("source") or "unknown").strip(),
            "title": title,
            "url": url,
            "publishedAt": (item.get("publishedAt") or "").strip(),
            "summary": (item.get("summary") or "").strip()[:160],
            "topicName": (item.get("topicName") or infer_topic(title, item.get("summary") or "")).strip(),
            "authorName": (item.get("authorName") or item.get("source") or "unknown").strip()
        }
    return list(dedup.values())


def main():
    all_items = []
    for name, url in RSS_SOURCES:
        all_items.extend(fetch_source(name, url))
    all_items.extend(fetch_wanandroid())

    all_items = normalize_items(all_items)

    if not all_items:
        all_items = FALLBACK_ITEMS

    payload = {
        "schemaVersion": 2,
        "fetchedAt": int(time.time()),
        "count": len(all_items),
        "items": all_items
    }

    os.makedirs("crawler/data", exist_ok=True)
    with open("crawler/data/programmer_seed.json", "w", encoding="utf-8") as f:
        json.dump(payload, f, ensure_ascii=False, indent=2)

    print(f"fetched {len(all_items)} items")


if __name__ == "__main__":
    main()

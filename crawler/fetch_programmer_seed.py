#!/usr/bin/env python3
import json
import os
import time
import urllib.request
import xml.etree.ElementTree as ET

RSS_SOURCES = [
    ("v2ex-tech", "https://www.v2ex.com/feed/tab/tech.xml"),
    ("v2ex-create", "https://www.v2ex.com/feed/tab/create.xml"),
]

FALLBACK_ITEMS = [
    {
        "source": "fallback",
        "title": "Java 微服务如何做链路追踪？",
        "url": "https://example.com/java-tracing",
        "publishedAt": "",
        "summary": "OpenTelemetry + SkyWalking 的落地路径"
    },
    {
        "source": "fallback",
        "title": "Android 启动优化 Checklist",
        "url": "https://example.com/android-startup",
        "publishedAt": "",
        "summary": "冷启动指标、主线程治理、IO 预加载"
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
            "summary": desc[:160]
        })
    return items


def fetch_source(name, url):
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        with urllib.request.urlopen(req, timeout=20) as resp:
            data = resp.read()
        return parse_rss(data, name)
    except Exception:
        return []


def main():
    all_items = []
    for name, url in RSS_SOURCES:
        all_items.extend(fetch_source(name, url))

    if not all_items:
        all_items = FALLBACK_ITEMS

    payload = {
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

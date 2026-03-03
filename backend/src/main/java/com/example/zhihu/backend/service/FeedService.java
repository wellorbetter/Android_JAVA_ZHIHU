package com.example.zhihu.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FeedService {
    private final ContentService contentService;

    private static final Path[] CRAWLER_CANDIDATES = new Path[]{
            Path.of("crawler", "data", "programmer_seed.json"),
            Path.of("..", "crawler", "data", "programmer_seed.json")
    };
    private static final Pattern ITEM_PATTERN = Pattern.compile(
            "\\{\\s*\"source\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"title\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"url\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"publishedAt\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"summary\"\\s*:\\s*\"([^\"]*)\"(?:\\s*,\\s*\"topicName\"\\s*:\\s*\"([^\"]*)\")?(?:\\s*,\\s*\"authorName\"\\s*:\\s*\"([^\"]*)\")?\\s*\\}",
            Pattern.DOTALL
    );

    public FeedService() {
        this(new ContentService());
    }

    public FeedService(ContentService contentService) {
        this.contentService = contentService;
    }

    public String recommendJson() {
        return recommendJson(null);
    }

    public String followingJson() {
        return followingJson(null);
    }

    public String hotJson() {
        return hotJson(null);
    }

    public String ideaJson() {
        return ideaJson(null);
    }

    public String recommendJson(String topicName) {
        List<FeedItem> allItems = new ArrayList<>(Arrays.asList(
                new FeedItem("q_1001", "Java 面试如何系统准备？", "给出一份从基础到项目的计划", "架构师A", 128, 36, "Backend"),
                new FeedItem("q_1002", "Android 冷启动优化清单", "从启动链路到基线指标全流程", "移动端B", 96, 21, "Android"),
                new FeedItem("q_1003", "Web 首屏性能从 3s 到 1s", "讲清预加载、代码拆分与缓存策略", "前端C", 84, 18, "Web"),
                new FeedItem("q_1004", "AI Agent 在业务中的落地方式", "RAG、工具调用与评估闭环", "AI工程师D", 173, 42, "AI")
        ));
        allItems.addAll(loadCrawlerItems());
        allItems.addAll(loadUserCreatedItems(topicName, 20));
        return buildFeedPage(filterByTopic(allItems, topicName), "cursor_2", false);
    }

    public String followingJson(String topicName) {
        List<FeedItem> allItems = Arrays.asList(
                new FeedItem("q_2001", "Android 性能优化实战", "启动优化、内存治理、卡顿排查", "移动端B", 64, 12, "Android"),
                new FeedItem("q_2002", "Spring Boot 服务治理清单", "可观测、熔断与限流的实践", "后端E", 55, 9, "Backend"),
                new FeedItem("q_2003", "AI 编码助手如何提效", "从 Prompt 到工作流自动化", "AI工程师D", 77, 15, "AI")
        );
        return buildFeedPage(filterByTopic(allItems, topicName), "", false);
    }

    public String hotJson(String topicName) {
        List<FeedItem> allItems = Arrays.asList(
                new FeedItem("q_3001", "2026 技术趋势：AI Native App", "从 Copilot 到 Agent 产品化", "观察者X", 321, 58, "AI"),
                new FeedItem("q_3002", "Android 架构升级实录", "从 MVC 迁移到 MVI 的踩坑总结", "移动端B", 287, 44, "Android"),
                new FeedItem("q_3003", "后端高并发设计模式", "缓存、队列、隔离与降级策略", "后端E", 245, 37, "Backend"),
                new FeedItem("q_3004", "Web 全栈工程化 2026", "Monorepo + Edge + RUM 实战", "前端C", 198, 31, "Web")
        );
        List<FeedItem> merged = new ArrayList<>(allItems);
        merged.addAll(loadUserCreatedItems(topicName, 10));
        return buildFeedPage(filterByTopic(merged, topicName), "", false);
    }

    public String ideaJson(String topicName) {
        List<FeedItem> allItems = Arrays.asList(
                new FeedItem("q_4001", "做一个开发者知识星球的 MVP", "先打通分类、发布、推荐三条闭环", "独立开发A", 36, 5, "Backend"),
                new FeedItem("q_4002", "KMP 多端共享最佳边界", "共享业务层，保留平台原生 UI", "移动端B", 42, 8, "Android"),
                new FeedItem("q_4003", "AI coding 工作流模板", "需求拆解、生成、review、回归", "AI工程师D", 51, 9, "AI")
        );
        return buildFeedPage(filterByTopic(allItems, topicName), "", false);
    }

    private List<FeedItem> filterByTopic(List<FeedItem> items, String topicName) {
        if (topicName == null || topicName.isEmpty() || "All".equalsIgnoreCase(topicName)) {
            return items;
        }
        List<FeedItem> filtered = new ArrayList<>();
        for (FeedItem item : items) {
            if (topicName.equalsIgnoreCase(item.topicName)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private String buildFeedPage(List<FeedItem> items, String nextCursor, boolean hasMore) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"items\":[");
        for (int i = 0; i < items.size(); i++) {
            FeedItem item = items.get(i);
            if (i > 0) {
                sb.append(",");
            }
            sb.append("{")
                    .append("\"contentId\":\"").append(escape(item.contentId)).append("\",")
                    .append("\"title\":\"").append(escape(item.title)).append("\",")
                    .append("\"summary\":\"").append(escape(item.summary)).append("\",")
                    .append("\"authorName\":\"").append(escape(item.authorName)).append("\",")
                    .append("\"likeCount\":").append(item.likeCount).append(",")
                    .append("\"commentCount\":").append(item.commentCount).append(",")
                    .append("\"topicName\":\"").append(escape(item.topicName)).append("\"")
                    .append("}");
        }
        sb.append("],\"nextCursor\":\"").append(escape(nextCursor)).append("\",\"hasMore\":").append(hasMore).append("}");
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private List<FeedItem> loadCrawlerItems() {
        List<FeedItem> result = new ArrayList<>();
        HashSet<String> dedup = new HashSet<>();
        String payload = readCrawlerSeed();
        if (payload == null || payload.isEmpty()) {
            return result;
        }
        Matcher matcher = ITEM_PATTERN.matcher(payload);
        int index = 0;
        while (matcher.find() && index < 20) {
            String source = matcher.group(1);
            String title = matcher.group(2);
            String summary = matcher.group(5);
            String topic = normalizeTopic(matcher.group(6));
            if (topic == null) {
                topic = inferTopic(title, summary);
            }
            String authorName = matcher.group(7);
            if (authorName == null || authorName.isEmpty()) {
                authorName = source.isEmpty() ? "crawler" : source;
            }
            String dedupKey = title + "|" + topic;
            if (dedup.contains(dedupKey)) {
                continue;
            }
            dedup.add(dedupKey);
            result.add(new FeedItem(
                    "seed_" + index,
                    title,
                    summary,
                    authorName,
                    Math.max(10, 100 - index * 3),
                    Math.max(1, 25 - index),
                    topic
            ));
            index++;
        }
        return result;
    }

    private List<FeedItem> loadUserCreatedItems(String topicName, int limit) {
        List<FeedItem> result = new ArrayList<>();
        List<ContentService.ContentEntry> entries = contentService.recentEntries(topicName, limit);
        for (ContentService.ContentEntry entry : entries) {
            result.add(new FeedItem(
                    entry.contentId,
                    entry.title,
                    entry.summary,
                    entry.authorName,
                    entry.likeCount,
                    entry.commentCount,
                    entry.topicName
            ));
        }
        return result;
    }

    private String readCrawlerSeed() {
        for (Path path : CRAWLER_CANDIDATES) {
            try {
                if (Files.exists(path)) {
                    return Files.readString(path, StandardCharsets.UTF_8);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private String inferTopic(String title, String summary) {
        String text = (title + " " + summary).toLowerCase();
        if (text.contains("android") || text.contains("kotlin") || text.contains("jetpack")) {
            return "Android";
        }
        if (text.contains("ios") || text.contains("swift")) {
            return "iOS";
        }
        if (text.contains("react") || text.contains("vue") || text.contains("javascript") || text.contains("frontend") || text.contains("web")) {
            return "Web";
        }
        if (text.contains("ai") || text.contains("llm") || text.contains("gpt") || text.contains("agent")) {
            return "AI";
        }
        return "Backend";
    }

    private String normalizeTopic(String topicName) {
        if (topicName == null || topicName.isEmpty()) {
            return null;
        }
        if ("ios".equalsIgnoreCase(topicName)) {
            return "iOS";
        }
        if ("ai".equalsIgnoreCase(topicName)) {
            return "AI";
        }
        if ("web".equalsIgnoreCase(topicName)) {
            return "Web";
        }
        if ("android".equalsIgnoreCase(topicName)) {
            return "Android";
        }
        if ("backend".equalsIgnoreCase(topicName)) {
            return "Backend";
        }
        return topicName;
    }

    private static class FeedItem {
        final String contentId;
        final String title;
        final String summary;
        final String authorName;
        final int likeCount;
        final int commentCount;
        final String topicName;

        FeedItem(String contentId, String title, String summary, String authorName, int likeCount, int commentCount, String topicName) {
            this.contentId = contentId;
            this.title = title;
            this.summary = summary;
            this.authorName = authorName;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
            this.topicName = topicName;
        }
    }
}

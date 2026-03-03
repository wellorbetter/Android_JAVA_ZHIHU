package com.example.zhihu.backend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentService {
    private static final Path[] STORE_CANDIDATES = new Path[]{
            Path.of("data", "content_store.tsv"),
            Path.of("backend", "data", "content_store.tsv"),
            Path.of("..", "backend", "data", "content_store.tsv")
    };
    private static final Path[] COMMENT_STORE_CANDIDATES = new Path[]{
            Path.of("data", "comment_store.tsv"),
            Path.of("backend", "data", "comment_store.tsv"),
            Path.of("..", "backend", "data", "comment_store.tsv")
    };
    private static final Pattern FIELD_PATTERN = Pattern.compile("\"(title|summary|topicName|comment)\"\\s*:\\s*\"([^\"]*)\"");
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public String createPostJson(String userName, String requestBody) {
        DraftPost draft = parseDraft(requestBody);
        if (draft.title.isEmpty() || draft.summary.isEmpty()) {
            return "{\"created\":false,\"error\":\"invalid_payload\"}";
        }
        ContentEntry entry = new ContentEntry(
                "p_" + System.currentTimeMillis(),
                draft.title,
                draft.summary,
                userName,
                normalizeTopic(draft.topicName),
                0,
                0,
                System.currentTimeMillis()
        );
        lock.writeLock().lock();
        try {
            List<ContentEntry> all = loadEntriesUnsafe();
            all.add(entry);
            saveEntriesUnsafe(all);
        } finally {
            lock.writeLock().unlock();
        }
            return "{\"created\":true,\"contentId\":\"" + jsonEscape(entry.contentId) + "\"}";
    }

    public String likeJson(String contentId) {
        if (contentId == null || contentId.isEmpty()) {
            return "{\"liked\":false,\"error\":\"content_id_required\"}";
        }
        lock.writeLock().lock();
        try {
            List<ContentEntry> all = loadEntriesUnsafe();
            for (ContentEntry item : all) {
                if (contentId.equals(item.contentId)) {
                    item.likeCount += 1;
                    saveEntriesUnsafe(all);
                    return "{\"liked\":true,\"contentId\":\"" + jsonEscape(contentId) + "\",\"likeCount\":" + item.likeCount + "}";
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
        return "{\"liked\":false,\"error\":\"not_found\"}";
    }

    public String addCommentJson(String contentId, String userName, String requestBody) {
        if (contentId == null || contentId.isEmpty()) {
            return "{\"commented\":false,\"error\":\"content_id_required\"}";
        }
        String comment = parseComment(requestBody);
        if (comment.isEmpty()) {
            return "{\"commented\":false,\"error\":\"invalid_comment\"}";
        }

        lock.writeLock().lock();
        try {
            List<ContentEntry> all = loadEntriesUnsafe();
            ContentEntry target = null;
            for (ContentEntry item : all) {
                if (contentId.equals(item.contentId)) {
                    target = item;
                    break;
                }
            }
            if (target == null) {
                return "{\"commented\":false,\"error\":\"not_found\"}";
            }

            target.commentCount += 1;
            saveEntriesUnsafe(all);

            List<CommentEntry> comments = loadCommentsUnsafe();
            CommentEntry newComment = new CommentEntry(
                    "c_" + System.currentTimeMillis(),
                    contentId,
                    userName == null || userName.isEmpty() ? "guest" : userName,
                    comment,
                    System.currentTimeMillis()
            );
            comments.add(newComment);
            saveCommentsUnsafe(comments);
            return "{\"commented\":true,\"commentId\":\"" + jsonEscape(newComment.commentId) + "\",\"contentId\":\"" + jsonEscape(contentId) + "\",\"commentCount\":" + target.commentCount + "}";
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String listCommentsJson(String contentId, String cursor, int limit) {
        if (contentId == null || contentId.isEmpty()) {
            return "{\"items\":[],\"nextCursor\":\"\",\"hasMore\":false}";
        }
        lock.readLock().lock();
        try {
            List<CommentEntry> comments = loadCommentsUnsafe();
            comments.sort(Comparator.comparingLong((CommentEntry it) -> it.createdAt).reversed());
            long cursorTime = parseLong(cursor);
            if (limit <= 0 || limit > 50) {
                limit = 20;
            }
            StringBuilder sb = new StringBuilder();
            int totalCount = 0;
            for (CommentEntry item : comments) {
                if (contentId.equals(item.contentId)) {
                    totalCount++;
                }
            }
            sb.append("{\"items\":[");
            int appended = 0;
            long nextCursor = 0L;
            boolean hasMore = false;
            for (CommentEntry item : comments) {
                if (!contentId.equals(item.contentId)) {
                    continue;
                }
                if (cursorTime > 0 && item.createdAt >= cursorTime) {
                    continue;
                }
                if (appended > 0) {
                    sb.append(",");
                }
                sb.append("{")
                        .append("\"commentId\":\"").append(jsonEscape(item.commentId)).append("\",")
                        .append("\"contentId\":\"").append(jsonEscape(item.contentId)).append("\",")
                        .append("\"authorName\":\"").append(jsonEscape(item.authorName)).append("\",")
                        .append("\"comment\":\"").append(jsonEscape(item.comment)).append("\",")
                        .append("\"createdAt\":").append(item.createdAt)
                        .append("}");
                appended++;
                nextCursor = item.createdAt;
                if (appended >= limit) {
                    break;
                }
            }
            if (nextCursor > 0) {
                for (CommentEntry item : comments) {
                    if (!contentId.equals(item.contentId)) {
                        continue;
                    }
                    if (item.createdAt < nextCursor) {
                        hasMore = true;
                        break;
                    }
                }
            }
            sb.append("],\"nextCursor\":\"").append(nextCursor > 0 ? nextCursor : "").append("\",\"hasMore\":").append(hasMore).append(",\"totalCount\":").append(totalCount).append("}");
            return sb.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public String deleteLatestOwnCommentJson(String contentId, String userName) {
        if (contentId == null || contentId.isEmpty()) {
            return "{\"deleted\":false,\"error\":\"content_id_required\"}";
        }
        if (userName == null || userName.isEmpty()) {
            return "{\"deleted\":false,\"error\":\"user_required\"}";
        }
        lock.writeLock().lock();
        try {
            List<CommentEntry> comments = loadCommentsUnsafe();
            CommentEntry target = null;
            int targetIndex = -1;
            for (int i = 0; i < comments.size(); i++) {
                CommentEntry item = comments.get(i);
                if (contentId.equals(item.contentId) && userName.equals(item.authorName)) {
                    if (target == null || item.createdAt > target.createdAt) {
                        target = item;
                        targetIndex = i;
                    }
                }
            }
            if (target == null) {
                return "{\"deleted\":false,\"error\":\"not_found\"}";
            }
            comments.remove(targetIndex);
            saveCommentsUnsafe(comments);

            List<ContentEntry> all = loadEntriesUnsafe();
            int newCount = 0;
            for (ContentEntry item : all) {
                if (contentId.equals(item.contentId)) {
                    item.commentCount = Math.max(0, item.commentCount - 1);
                    newCount = item.commentCount;
                    break;
                }
            }
            saveEntriesUnsafe(all);
            return "{\"deleted\":true,\"commentId\":\"" + jsonEscape(target.commentId) + "\",\"contentId\":\"" + jsonEscape(contentId) + "\",\"commentCount\":" + newCount + "}";
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<ContentEntry> recentEntries(String topicName, int limit) {
        lock.readLock().lock();
        try {
            List<ContentEntry> all = loadEntriesUnsafe();
            List<ContentEntry> filtered = new ArrayList<>();
            for (ContentEntry item : all) {
                if (topicName == null || topicName.isEmpty() || "All".equalsIgnoreCase(topicName) || topicName.equalsIgnoreCase(item.topicName)) {
                    filtered.add(item);
                }
            }
            filtered.sort(Comparator.comparingLong((ContentEntry it) -> it.createdAt).reversed());
            if (filtered.size() > limit) {
                return new ArrayList<>(filtered.subList(0, limit));
            }
            return filtered;
        } finally {
            lock.readLock().unlock();
        }
    }

    private DraftPost parseDraft(String body) {
        if (body == null) {
            return new DraftPost("", "", "");
        }
        String title = "";
        String summary = "";
        String topicName = "";
        Matcher matcher = FIELD_PATTERN.matcher(body);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if ("title".equals(key)) {
                title = value.trim();
            } else if ("summary".equals(key)) {
                summary = value.trim();
            } else if ("topicName".equals(key)) {
                topicName = value.trim();
            }
        }
        return new DraftPost(title, summary, topicName);
    }

    private String parseComment(String body) {
        if (body == null) {
            return "";
        }
        Matcher matcher = FIELD_PATTERN.matcher(body);
        while (matcher.find()) {
            if ("comment".equals(matcher.group(1))) {
                return matcher.group(2).trim();
            }
        }
        return "";
    }

    private List<ContentEntry> loadEntriesUnsafe() {
        Path path = resolveStorePath();
        List<ContentEntry> items = new ArrayList<>();
        try {
            ensureStore(path);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\t", -1);
                if (parts.length != 8) {
                    continue;
                }
                items.add(new ContentEntry(
                        parts[0],
                        unescapeTsv(parts[1]),
                        unescapeTsv(parts[2]),
                        unescapeTsv(parts[3]),
                        unescapeTsv(parts[4]),
                        parseInt(parts[5]),
                        parseInt(parts[6]),
                        parseLong(parts[7])
                ));
            }
        } catch (IOException ignored) {
        }
        return items;
    }

    private void saveEntriesUnsafe(List<ContentEntry> items) {
        Path path = resolveStorePath();
        try {
            ensureStore(path);
            List<String> lines = new ArrayList<>();
            for (ContentEntry it : items) {
                lines.add(String.join("\t",
                        it.contentId,
                        escapeTsv(it.title),
                        escapeTsv(it.summary),
                        escapeTsv(it.authorName),
                        escapeTsv(it.topicName),
                        String.valueOf(it.likeCount),
                        String.valueOf(it.commentCount),
                        String.valueOf(it.createdAt)
                ));
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private List<CommentEntry> loadCommentsUnsafe() {
        Path path = resolveCommentStorePath();
        List<CommentEntry> items = new ArrayList<>();
        try {
            ensureStore(path);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\t", -1);
                if (parts.length != 5) {
                    continue;
                }
                items.add(new CommentEntry(
                        parts[0],
                        parts[1],
                        unescapeTsv(parts[2]),
                        unescapeTsv(parts[3]),
                        parseLong(parts[4])
                ));
            }
        } catch (IOException ignored) {
        }
        return items;
    }

    private void saveCommentsUnsafe(List<CommentEntry> items) {
        Path path = resolveCommentStorePath();
        try {
            ensureStore(path);
            List<String> lines = new ArrayList<>();
            for (CommentEntry item : items) {
                lines.add(String.join("\t",
                        item.commentId,
                        item.contentId,
                        escapeTsv(item.authorName),
                        escapeTsv(item.comment),
                        String.valueOf(item.createdAt)
                ));
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private void ensureStore(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    private Path resolveStorePath() {
        for (Path candidate : STORE_CANDIDATES) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        return STORE_CANDIDATES[0];
    }

    private Path resolveCommentStorePath() {
        for (Path candidate : COMMENT_STORE_CANDIDATES) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        return COMMENT_STORE_CANDIDATES[0];
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return 0;
        }
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception ignored) {
            return 0L;
        }
    }

    private String normalizeTopic(String topic) {
        if (topic == null || topic.isBlank()) {
            return "Backend";
        }
        if ("ios".equalsIgnoreCase(topic)) {
            return "iOS";
        }
        if ("ai".equalsIgnoreCase(topic)) {
            return "AI";
        }
        if ("web".equalsIgnoreCase(topic)) {
            return "Web";
        }
        if ("android".equalsIgnoreCase(topic)) {
            return "Android";
        }
        if ("backend".equalsIgnoreCase(topic)) {
            return "Backend";
        }
        return topic;
    }

    private String escapeTsv(String value) {
        return value.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n");
    }

    private String unescapeTsv(String value) {
        return value.replace("\\t", "\t").replace("\\n", "\n").replace("\\\\", "\\");
    }

    private String jsonEscape(String value) {
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

    private static class DraftPost {
        final String title;
        final String summary;
        final String topicName;

        DraftPost(String title, String summary, String topicName) {
            this.title = title;
            this.summary = summary;
            this.topicName = topicName;
        }
    }

    public static class ContentEntry {
        public final String contentId;
        public final String title;
        public final String summary;
        public final String authorName;
        public final String topicName;
        public int likeCount;
        public int commentCount;
        public final long createdAt;

        public ContentEntry(String contentId, String title, String summary, String authorName, String topicName, int likeCount, int commentCount, long createdAt) {
            this.contentId = contentId;
            this.title = title;
            this.summary = summary;
            this.authorName = authorName;
            this.topicName = topicName;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
            this.createdAt = createdAt;
        }
    }

    private static class CommentEntry {
        final String commentId;
        final String contentId;
        final String authorName;
        final String comment;
        final long createdAt;

        private CommentEntry(String commentId, String contentId, String authorName, String comment, long createdAt) {
            this.commentId = commentId;
            this.contentId = contentId;
            this.authorName = authorName;
            this.comment = comment;
            this.createdAt = createdAt;
        }
    }
}

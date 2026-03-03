package com.example.zhihu.backend.http;

import com.example.zhihu.backend.service.AuthService;
import com.example.zhihu.backend.service.ContentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ContentHandler implements HttpHandler {
    private final AuthService authService;
    private final ContentService contentService;

    public ContentHandler(AuthService authService, ContentService contentService) {
        this.authService = authService;
        this.contentService = contentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        String token = extractBearerToken(exchange.getRequestHeaders().getFirst("Authorization"));
        if (!authService.isAuthorized(token)) {
            writeJson(exchange, 401, JsonResponse.error(40101, "AUTH_REQUIRED"));
            return;
        }

        if ("POST".equals(method) && "/v1/content/posts".equals(path)) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String data = contentService.createPostJson(authService.userOf(token), body);
            writeJson(exchange, 200, JsonResponse.success(data));
            return;
        }

        if ("POST".equals(method) && path.startsWith("/v1/content/") && path.endsWith("/like")) {
            String contentId = path.substring("/v1/content/".length(), path.length() - "/like".length());
            String data = contentService.likeJson(contentId);
            writeJson(exchange, 200, JsonResponse.success(data));
            return;
        }

        if ("POST".equals(method) && path.startsWith("/v1/content/") && path.endsWith("/comments")) {
            String contentId = path.substring("/v1/content/".length(), path.length() - "/comments".length());
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String data = contentService.addCommentJson(contentId, authService.userOf(token), body);
            writeJson(exchange, 200, JsonResponse.success(data));
            return;
        }

        if ("GET".equals(method) && path.startsWith("/v1/content/") && path.endsWith("/comments")) {
            String contentId = path.substring("/v1/content/".length(), path.length() - "/comments".length());
            String cursor = queryParam(exchange.getRequestURI(), "cursor");
            int limit = parseInt(queryParam(exchange.getRequestURI(), "limit"), 20);
            String data = contentService.listCommentsJson(contentId, cursor, limit);
            writeJson(exchange, 200, JsonResponse.success(data));
            return;
        }

        if ("POST".equals(method) && path.startsWith("/v1/content/") && path.endsWith("/comments/delete-latest")) {
            String contentId = path.substring("/v1/content/".length(), path.length() - "/comments/delete-latest".length());
            String data = contentService.deleteLatestOwnCommentJson(contentId, authService.userOf(token));
            writeJson(exchange, 200, JsonResponse.success(data));
            return;
        }

        writeJson(exchange, 404, JsonResponse.error(404, "not_found"));
    }

    private String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring("Bearer ".length()).trim();
    }

    private void writeJson(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String queryParam(URI uri, String key) {
        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) {
            return null;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && key.equals(kv[0])) {
                return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }
}

package com.example.zhihu.backend;

import com.example.zhihu.backend.http.AuthHandler;
import com.example.zhihu.backend.http.ContentHandler;
import com.example.zhihu.backend.http.JsonResponse;
import com.example.zhihu.backend.http.ProtectedHandler;
import com.example.zhihu.backend.model.SessionStore;
import com.example.zhihu.backend.service.AuthService;
import com.example.zhihu.backend.service.CrawlerSeedService;
import com.example.zhihu.backend.service.ContentService;
import com.example.zhihu.backend.service.FeedService;
import com.example.zhihu.backend.service.TopicService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class BackendServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        SessionStore sessionStore = new SessionStore();
        AuthService authService = new AuthService(sessionStore);
        ContentService contentService = new ContentService();
        FeedService feedService = new FeedService(contentService);
        TopicService topicService = new TopicService();
        CrawlerSeedService crawlerSeedService = new CrawlerSeedService();

        server.createContext("/v1/auth/login", new AuthHandler(authService));
        server.createContext("/v1/auth/logout", new AuthHandler(authService));
        server.createContext("/v1/feed/recommend", new ProtectedHandler(authService,
                exchange -> feedService.recommendJson(queryParam(exchange.getRequestURI(), "topic"))));
        server.createContext("/v1/feed/following", new ProtectedHandler(authService,
                exchange -> feedService.followingJson(queryParam(exchange.getRequestURI(), "topic"))));
        server.createContext("/v1/feed/hot", new ProtectedHandler(authService,
                exchange -> feedService.hotJson(queryParam(exchange.getRequestURI(), "topic"))));
        server.createContext("/v1/feed/idea", new ProtectedHandler(authService,
                exchange -> feedService.ideaJson(queryParam(exchange.getRequestURI(), "topic"))));
        server.createContext("/v1/content/posts", new ContentHandler(authService, contentService));
        server.createContext("/v1/content/", new ContentHandler(authService, contentService));
        server.createContext("/v1/topics", new ProtectedHandler(authService, topicService::topicsJson));
        server.createContext("/v1/crawler/seed", new ProtectedHandler(authService, crawlerSeedService::seedJson));
        server.createContext("/v1/system/health", exchange -> {
            if (!"GET".equals(exchange.getRequestMethod())) {
                writeJson(exchange, 405, JsonResponse.error(405, "method_not_allowed"));
                return;
            }
            writeJson(exchange, 200, JsonResponse.success("{\"status\":\"UP\",\"serverTime\":" + System.currentTimeMillis() + "}"));
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Backend server started on http://0.0.0.0:8080");
    }

    private static String queryParam(URI uri, String key) {
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

    private static void writeJson(com.sun.net.httpserver.HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

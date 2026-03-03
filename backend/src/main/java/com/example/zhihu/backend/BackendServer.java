package com.example.zhihu.backend;

import com.example.zhihu.backend.http.AuthHandler;
import com.example.zhihu.backend.http.ProtectedHandler;
import com.example.zhihu.backend.model.SessionStore;
import com.example.zhihu.backend.service.AuthService;
import com.example.zhihu.backend.service.FeedService;
import com.example.zhihu.backend.service.TopicService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class BackendServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        SessionStore sessionStore = new SessionStore();
        AuthService authService = new AuthService(sessionStore);
        FeedService feedService = new FeedService();
        TopicService topicService = new TopicService();
        CrawlerSeedService crawlerSeedService = new CrawlerSeedService();

        server.createContext("/v1/auth/login", new AuthHandler(authService));
        server.createContext("/v1/auth/logout", new AuthHandler(authService));
        server.createContext("/v1/feed/recommend", new ProtectedHandler(authService, feedService::recommendJson));
        server.createContext("/v1/feed/following", new ProtectedHandler(authService, feedService::followingJson));
        server.createContext("/v1/topics", new ProtectedHandler(authService, topicService::topicsJson));
        server.createContext("/v1/crawler/seed", new ProtectedHandler(authService, crawlerSeedService::seedJson));

        server.setExecutor(null);
        server.start();

        System.out.println("Backend server started on http://0.0.0.0:8080");
    }
}

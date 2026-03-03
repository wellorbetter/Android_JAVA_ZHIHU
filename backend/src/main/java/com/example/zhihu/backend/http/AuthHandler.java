package com.example.zhihu.backend.http;

import com.example.zhihu.backend.service.AuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class AuthHandler implements HttpHandler {
    private final AuthService authService;

    public AuthHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if ("POST".equals(exchange.getRequestMethod()) && "/v1/auth/login".equals(path)) {
            String token = authService.login("android_user");
            String body = JsonResponse.success("{\"accessToken\":\"" + token + "\",\"refreshToken\":\"refresh_" + token + "\",\"expireAt\":1893456000000}");
            writeJson(exchange, 200, body);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod()) && "/v1/auth/logout".equals(path)) {
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            String token = extractBearerToken(authHeader);
            authService.logout(token);
            writeJson(exchange, 200, JsonResponse.success("{}"));
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
}

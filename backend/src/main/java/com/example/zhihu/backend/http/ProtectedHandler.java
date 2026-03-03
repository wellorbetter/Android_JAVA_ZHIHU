package com.example.zhihu.backend.http;

import com.example.zhihu.backend.service.AuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class ProtectedHandler implements HttpHandler {
    private final AuthService authService;
    private final Supplier<String> jsonDataSupplier;

    public ProtectedHandler(AuthService authService, Supplier<String> jsonDataSupplier) {
        this.authService = authService;
        this.jsonDataSupplier = jsonDataSupplier;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            writeJson(exchange, 405, JsonResponse.error(405, "method_not_allowed"));
            return;
        }

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        String token = extractBearerToken(authHeader);
        if (!authService.isAuthorized(token)) {
            writeJson(exchange, 401, JsonResponse.error(40101, "AUTH_REQUIRED"));
            return;
        }

        String body = JsonResponse.success(jsonDataSupplier.get());
        writeJson(exchange, 200, body);
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

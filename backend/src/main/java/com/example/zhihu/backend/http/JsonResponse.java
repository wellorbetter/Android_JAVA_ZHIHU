package com.example.zhihu.backend.http;

public final class JsonResponse {
    private JsonResponse() {
    }

    public static String success(String dataJson) {
        return "{\"code\":0,\"message\":\"ok\",\"data\":" + dataJson + "}";
    }

    public static String error(int code, String message) {
        return "{\"code\":" + code + ",\"message\":\"" + message + "\",\"data\":null}";
    }
}

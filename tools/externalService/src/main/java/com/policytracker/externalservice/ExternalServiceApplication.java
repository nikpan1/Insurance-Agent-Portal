package com.policytracker.externalservice;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExternalServiceApplication {

    private static final Pattern STRING_FIELD_PATTERN_TEMPLATE = Pattern.compile("\"%s\"\\s*:\\s*\"([^\"]+)\"");

    private ExternalServiceApplication() {
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "18080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", ExternalServiceApplication::handle);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("External mock service running on http://localhost:" + port);
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = normalizePath(exchange.getRequestURI().getPath());
        String body = readBody(exchange.getRequestBody());

        if ("POST".equals(method) && "/insurance/user-data".equals(path)) {
            String externalUserId = extractStringField(body, "externalUserId", "EXT-USER-12345");
            String response = "{" +
                    "\"externalUserId\":\"" + escape(externalUserId) + "\"," +
                    "\"firstName\":\"John\"," +
                    "\"lastName\":\"Doe\"," +
                    "\"activePolicies\":[{" +
                    "\"policyId\":\"POL-10001\"," +
                    "\"type\":\"HEALTH\"," +
                    "\"status\":\"ACTIVE\"" +
                    "}]," +
                    "\"riskScore\":0.2" +
                    "}";
            sendJson(exchange, 200, response);
            return;
        }

        if ("PUT".equals(method) && "/insurance/status".equals(path)) {
            String policyId = extractStringField(body, "policyId", "POL-10001");
            String status = extractStringField(body, "status", "ACTIVE");
            String response = "{" +
                    "\"policyId\":\"" + escape(policyId) + "\"," +
                    "\"status\":\"" + escape(status) + "\"," +
                    "\"updatedAt\":\"" + Instant.now() + "\"" +
                    "}";
            sendJson(exchange, 200, response);
            return;
        }

        if ("GET".equals(method) && "/health".equals(path)) {
            sendJson(exchange, 200, "{\"status\":\"ok\"}");
            return;
        }

        sendJson(exchange, 404, "{\"message\":\"Not found\"}");
    }

    private static String normalizePath(String path) {
        String normalized = path == null ? "" : path;
        if (normalized.startsWith("/mock-external-insurance")) {
            normalized = normalized.substring("/mock-external-insurance".length());
        }
        if (normalized.startsWith("/v1")) {
            normalized = normalized.substring("/v1".length());
        }
        return normalized;
    }

    private static String readBody(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String extractStringField(String json, String key, String fallback) {
        Pattern pattern = Pattern.compile(String.format(STRING_FIELD_PATTERN_TEMPLATE.pattern(), Pattern.quote(key)));
        Matcher matcher = pattern.matcher(json == null ? "" : json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return fallback;
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }
}

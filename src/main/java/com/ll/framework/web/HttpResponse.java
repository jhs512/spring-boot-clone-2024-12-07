package com.ll.framework.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class HttpResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int status = 200;
    private String statusMessage = "OK";
    private Map<String, String> headers = new HashMap<>();
    private StringBuilder body = new StringBuilder();

    public HttpResponse() {
        setHeader("Content-Type", "text/html");
    }

    public void setStatus(int status) {
        this.status = status;
        this.statusMessage = switch (status) {
            case 200 -> "OK";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }

    public void setJsonBody(Object obj) throws JsonProcessingException {
        setHeader("Content-Type", "application/json");
        String jsonBody = objectMapper.writeValueAsString(obj);
        setBody(jsonBody);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setBody(String body) {
        this.body = new StringBuilder(body);
        setHeader("Content-Length", String.valueOf(body.length()));
    }

    public void write(String content) {
        body.append(content);
        setHeader("Content-Length", String.valueOf(body.length()));
    }

    public String toString() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(String.format("HTTP/1.1 %d %s\r\n", status, statusMessage));

        // 헤더 추가
        headers.forEach((name, value)
                -> responseBuilder.append(String.format("%s: %s\r\n", name, value))
        );

        // 빈 줄로 헤더와 본문 구분
        responseBuilder.append("\r\n");

        // 본문 추가
        responseBuilder.append(body);

        return responseBuilder.toString();
    }
}
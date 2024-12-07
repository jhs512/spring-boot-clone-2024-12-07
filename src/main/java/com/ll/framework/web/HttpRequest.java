package com.ll.framework.web;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HttpRequest {

    private String method;
    private String requestURI;
    private String protocol;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    public void setHeader(String name, String value) {
        headers.put(name.toLowerCase(), value);
    }
}
package com.ferran.http;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final static String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private final static String LOCATION_HEADER_NAME = "Location";

    private HttpStatus statusCode;
    private String mediaType = MediaType.TEXT_HTML;
    private HttpExchange httpExchange;
    private Map<String, String> responseHeaders;

    public HttpResponse(HttpStatus statusCode, HttpExchange httpExchange, Map<String, String> responseHeaders) {
        this.statusCode = statusCode;
        this.httpExchange = httpExchange;
        this.responseHeaders = responseHeaders;
    }

    public HttpResponse(HttpStatus statusCode, HttpExchange httpExchange) {
        this.statusCode = statusCode;
        this.httpExchange = httpExchange;
        this.responseHeaders = new HashMap<String, String>();
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeader(String key, String value){
        responseHeaders.put(key, value);
    }

    public void redirect(String url) throws IOException{
        httpExchange.getResponseHeaders().add(LOCATION_HEADER_NAME, url);
        httpExchange.sendResponseHeaders(HttpStatus.FOUND.getValue(), -1);
    }

    public void setCookie(HttpCookie cookie, final String expireDate){
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";")
                .append("Path=").append(cookie.getPath()).append(";");

        if(expireDate != null) {
            sb.append("expires=").append(expireDate);
        }
        httpExchange.getResponseHeaders().add("Set-Cookie", sb.toString());
    }

    public void sendResponse(String response) throws IOException{
        responseHeaders.forEach(
                (k , v) -> {
                    httpExchange.getResponseHeaders().add(k, v);
                }
        );
        httpExchange.sendResponseHeaders(this.statusCode.getValue(), response.length());
        try(OutputStream responseStream = this.httpExchange.getResponseBody()){
            responseStream.write(response.getBytes());
        }
    }
}

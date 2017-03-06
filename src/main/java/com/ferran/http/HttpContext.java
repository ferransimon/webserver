package com.ferran.http;

import javafx.util.Pair;

import java.net.HttpCookie;
import java.util.Map;
import java.util.Optional;

public class HttpContext {

    private Map<String, HttpCookie> cookies;
    private Map<String, String> formParams;
    private String requestBody;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;
    private Pair<String, String> authHeaders;

    public void setCookies(Map<String, HttpCookie> cookies) {
        this.cookies = cookies;
    }

    public void setFormParams(Map<String, String> formParams) {
        this.formParams = formParams;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }

    public void setAuthHeaders(Pair<String, String> authHeaders) {
        this.authHeaders = authHeaders;
    }

    public Optional<Map<String, HttpCookie>> getCookies() {
        return Optional.ofNullable(cookies);
    }

    public Optional<Map<String, String>> getFormParams() {
        return Optional.ofNullable(formParams);
    }

    public Optional<String> getRequestBody() {
        return Optional.ofNullable(requestBody);
    }

    public Optional<Map<String, String>> getQueryParams() {
        return Optional.ofNullable(queryParams);
    }

    public Optional<Map<String, String>> getPathParams() {
        return Optional.ofNullable(pathParams);
    }

    public Optional<Pair<String, String>> getAuthHeaders() {
        return Optional.ofNullable(authHeaders);
    }
}

package com.ferran.http.utils;


import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.HttpContext;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HttpExchangeContextParser implements HttpContextParser<HttpExchange> {

    private final static Pattern formDataPattern = Pattern.compile("([A-Za-z0-9]+=[A-Za-z0-9]*&?)+");
    private final static String COOKIE_HEADER = "Cookie";
    private final static String AUTH_HEADER = "Authorization";

    public HttpExchangeContextParser() {

    }

    @Override
    public HttpContext createContext(HttpExchange request) throws IOException{
        HttpContext context = new HttpContext();
        context.setCookies(getCookies(request));
        String body = getBody(request);
        context.setRequestBody(body);
        context.setFormParams(getFormParams(body));
        context.setQueryParams(getQueryParams(request));
        context.setAuthHeaders(getAuthHeaders(request));

        return context;
    }

    private Pair<String, String> getAuthHeaders(HttpExchange httpExchange){
        String authHeader = httpExchange.getRequestHeaders().getFirst(AUTH_HEADER);
        if(authHeader != null){
            String[] authHeaderValues = authHeader.split(" ");
            if(authHeaderValues.length == 2){
                return new Pair<>(authHeaderValues[0], authHeaderValues[1]);
            }
        }

        return null;
    }

    private Map<String, HttpCookie> getCookies(HttpExchange httpExchange){
        String cookieString = httpExchange.getRequestHeaders().getFirst(COOKIE_HEADER);
        if(cookieString != null){
            return Arrays.asList(cookieString.split(";")).stream()
                    .filter(line -> line.split("=").length == 2)
                    .map(
                            line -> {
                                return line.split("=");
                            }
                    ).collect(Collectors.toMap(
                            key -> key[0].trim(),
                            val -> new HttpCookie(val[0], val[1]),
                            (key1, key2) ->{
                                return key1;
                            }
                        )
                    );
        }

        return null;
    }

    private String getBody(HttpExchange httpExchange) throws IOException{
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(httpExchange.getRequestBody()))){
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private Map<String, String> getFormParams(String body){
        if(body == null) return null;
        return Stream.of(body.split("\n")).filter(
                line -> formDataPattern.matcher(line).find()
        ).map(
                line -> {
                    return Arrays.asList(line.split("&"));
                }
        ).flatMap(Collection::stream)
                .map(param -> param.split("="))
                .collect(Collectors.toMap(
                        key -> key[0].trim(),
                        val -> val[1]
                ));
    }

    private Map<String,String> getQueryParams(HttpExchange httpExchange){
        String query = httpExchange.getRequestURI().getQuery();
        if(query == null) return null;
        return Stream.of(query.split("&"))
                .map(
                        params -> {
                            return Arrays.asList(params.split("&"));
                        }
                ).flatMap(Collection::stream)
                .map(param -> param.split("="))
                .collect(Collectors.toMap(
                        key -> key[0].trim(),
                        val -> val[1]
                ));
    }
}

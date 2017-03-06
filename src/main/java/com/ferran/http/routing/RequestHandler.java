package com.ferran.http.routing;

import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.HttpResponse;

import java.io.IOException;


public interface RequestHandler {

    void handle(final HttpExchange request, final HttpResponse response) throws IOException;

}

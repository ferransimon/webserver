package com.ferran.http.routing;


import com.sun.net.httpserver.HttpExchange;

public interface Gateway {

    RequestHandler getHandler(HttpExchange request);

}

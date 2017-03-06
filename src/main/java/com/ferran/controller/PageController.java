package com.ferran.controller;

import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.Constants;
import com.ferran.http.HttpResponse;
import com.ferran.http.routing.RequestHandler;
import com.ferran.http.render.Render;
import com.ferran.http.session.Session;
import com.ferran.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class PageController implements RequestHandler {

    private String pageName;
    private Render render;

    public PageController(String pageName, Render render) {
        this.pageName = pageName;
        this.render = render;
    }

    @Override
    public void handle(HttpExchange request, HttpResponse httpResponse) throws IOException {
        Optional<Session<User>> session = (Optional<Session<User>>) request.getAttribute(Constants.REUQEST_SESSION_KEY_NAME);
        Map<String, String> variables = new HashMap<>();
        variables.put("username",session.get().getUser().getUsername());
        variables.put("title", pageName);
        httpResponse.sendResponse(render.render("page", variables));
    }
}

package com.ferran.controller;


import com.ferran.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.Constants;
import com.ferran.http.HttpResponse;
import com.ferran.http.routing.RequestHandler;
import com.ferran.http.session.Session;
import com.ferran.service.LoginService;

import java.io.IOException;
import java.util.Optional;

public class LogoutController implements RequestHandler{

    private LoginService<Session<User>> loginService;

    public LogoutController(LoginService<Session<User>> loginService){
        this.loginService = loginService;
    }

    @Override
    public void handle(HttpExchange request, HttpResponse response) throws IOException {
        Optional<Session<User>> session = (Optional<Session<User>>)request.getAttribute(Constants.REUQEST_SESSION_KEY_NAME);
        session.ifPresent(
                s -> this.loginService.doLogout(s)
        );

        response.redirect("/login");

    }
}

package com.ferran.controller.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.dao.DAO;
import com.ferran.http.HttpResponse;
import com.ferran.http.routing.RequestHandler;
import com.ferran.model.User;
import com.ferran.service.UserService;

import java.io.IOException;

public class ListUsersController implements RequestHandler {

    private Gson gson;
    private UserService userService;

    public ListUsersController(Gson gson, UserService userService) {
        this.gson = gson;
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange request, HttpResponse response) throws IOException {

        response.sendResponse(gson.toJson(userService.getAll()));

    }
}

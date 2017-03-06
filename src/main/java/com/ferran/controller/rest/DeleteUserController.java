package com.ferran.controller.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.Constants;
import com.ferran.http.HttpContext;
import com.ferran.http.HttpResponse;
import com.ferran.http.HttpStatus;
import com.ferran.http.routing.RequestHandler;
import com.ferran.model.User;
import com.ferran.service.UserService;

import java.io.IOException;
import java.util.Optional;

public class DeleteUserController implements RequestHandler {

    private Gson gson;
    private UserService userService;

    public DeleteUserController(Gson gson, UserService userService) {
        this.gson = gson;
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange request, HttpResponse response) throws IOException {
        HttpContext params = (HttpContext) request.getAttribute(Constants.REUQEST_PARAMS_KEY_NAME);
        String userId = params.getPathParams().get().get("userId");
        Optional<User> user = userService.deleteUser(userId);
        if(user.isPresent()){
            response.sendResponse(gson.toJson(user.get()));
        }else{
            response.setStatusCode(HttpStatus.NOT_FOUND);
            response.sendResponse("{\"error\":\"user not found\"}");
        }

    }
}

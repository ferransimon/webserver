package com.ferran.controller.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.Constants;
import com.ferran.http.HttpContext;
import com.ferran.http.HttpResponse;
import com.ferran.http.HttpStatus;
import com.ferran.http.routing.RequestHandler;
import com.ferran.model.NewUser;
import com.ferran.model.User;
import com.ferran.service.UserService;

import java.io.IOException;
import java.util.Optional;


public class UpdateUserController implements RequestHandler {

    private Gson gson;
    private UserService userService;

    public UpdateUserController(Gson gson, UserService userService) {
        this.gson = gson;
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange request, HttpResponse response) throws IOException {
        HttpContext params = (HttpContext) request.getAttribute(Constants.REUQEST_PARAMS_KEY_NAME);
        NewUser newUser = gson.fromJson(params.getRequestBody().get(), NewUser.class);
        User user = new User(
                newUser.getUsername(),
                newUser.getPassword(),
                newUser.getRoles()
        );
        Optional<User> optUser = userService.updateUser(user);
        if(optUser.isPresent()){
            response.sendResponse(gson.toJson(optUser.get()));
        }else{
            response.setStatusCode(HttpStatus.NOT_FOUND);
            response.sendResponse("{\"error\":\"user not found\"}");
        }
    }
}

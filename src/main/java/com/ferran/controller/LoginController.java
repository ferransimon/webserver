package com.ferran.controller;

import com.ferran.http.Constants;
import com.ferran.http.HttpContext;
import com.ferran.http.HttpResponse;
import com.ferran.http.RequestMethod;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.render.Render;
import com.ferran.http.routing.RequestHandler;
import com.ferran.http.session.Session;
import com.ferran.service.LoginService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


public class LoginController implements RequestHandler {

    private static final String DEFAULT_HOME_PAGE = "home";

    private Render render;
    private LoginService<Session> loginService;

    public LoginController(Render render, LoginService<Session> loginService){
        this.render = render;
        this.loginService = loginService;
    }

    @Override
    public void handle(HttpExchange request, HttpResponse httpResponse) throws IOException {
        HttpContext params = (HttpContext) request.getAttribute(Constants.REUQEST_PARAMS_KEY_NAME);
        Optional<Session> contextSession = (Optional<Session>)request.getAttribute(Constants.REUQEST_SESSION_KEY_NAME);
        String redirect = null;
        if(params.getQueryParams().isPresent()) {
            redirect = params.getQueryParams().get().getOrDefault("redirect", DEFAULT_HOME_PAGE);
        }

        if(contextSession.isPresent()){
            httpResponse.redirect(redirect != null ? redirect : DEFAULT_HOME_PAGE);
        }

        if(request.getRequestMethod().equals(RequestMethod.GET.name())) {
            HashMap<String, String> variables = new HashMap<>();
            variables.put("title", "Login");
            variables.put("action", redirect != null ? "/login?redirect="+redirect : "/login");
            httpResponse.sendResponse(
                    render.render(
                            "login",
                            variables
                    )
            );
            return;
        }else if(request.getRequestMethod().equals(RequestMethod.POST.name())){
            params.getFormParams().ifPresent(
                    map -> {
                        if(map.containsKey("username") && map.containsKey("password")){
                            Optional<Session> session = loginService.doLogin(map.get("username"), map.get("password"), httpResponse);
                            if(session.isPresent()){
                                String defaultPath = DEFAULT_HOME_PAGE;
                                if(params.getQueryParams().isPresent()) {
                                    defaultPath = params.getQueryParams().get().getOrDefault("redirect", defaultPath);
                                }
                                try {
                                    httpResponse.redirect(defaultPath);
                                } catch (IOException e) {
                                    throw new RuntimeException();
                                }
                                return;
                            }
                        }
                    }
            );
        }
        httpResponse.redirect(redirect != null ? "/login?redirect="+redirect : "/login");
    }
}

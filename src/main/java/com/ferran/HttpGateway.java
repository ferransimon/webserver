package com.ferran;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.ferran.http.*;
import com.ferran.http.routing.Gateway;
import com.ferran.http.routing.RequestHandler;
import com.ferran.http.routing.RequestMapping;
import com.ferran.http.routing.Router;
import com.ferran.http.session.Session;
import com.ferran.model.User;
import com.ferran.service.RoleManager;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpGateway implements Gateway, HttpHandler {

    private Router<RequestMapping> router;
    private final static RequestHandler NOT_FOUND_PAGE = (req, resp) -> {
        resp.setStatusCode(HttpStatus.NOT_FOUND);
        resp.sendResponse("Page not found");
    };

    private final static RequestHandler REDIRECT_TO_LOGIN = (req, resp) -> {
        resp.setStatusCode(HttpStatus.FOUND);
        resp.redirect("/login?redirect="+req.getRequestURI().getPath());
    };

    private final static RequestHandler FORBIDDEN_PAGE = (req, resp) -> {
        resp.setStatusCode(HttpStatus.FORBIDDEN);
        resp.sendResponse(HttpStatus.FORBIDDEN.getDescription());
    };

    private RoleManager<User.Role, User> roleService;

    public HttpGateway(Router<RequestMapping> router, RoleManager<User.Role, User> roleService){
        this.router = router;
        this.roleService = roleService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        getHandler(httpExchange).handle(httpExchange, new HttpResponse(HttpStatus.OK, httpExchange));
    }

    @Override
    public RequestHandler getHandler(HttpExchange request) {
        final String requestMethod = request.getRequestMethod().toUpperCase();
        final String requestURI = request.getRequestURI().getPath();
        HttpContext params = (HttpContext) request.getAttribute(Constants.REUQEST_PARAMS_KEY_NAME);
        Optional<RequestMapping> optionalRequestMapping = this.router.getRoute(requestURI, RequestMethod.valueOf(requestMethod));
        if(!optionalRequestMapping.isPresent()) return NOT_FOUND_PAGE;

        RequestMapping requestMapping = optionalRequestMapping.get();
        Optional<Session<User>> session = (Optional<Session<User>>)request.getAttribute(Constants.REUQEST_SESSION_KEY_NAME);
        params.setPathParams(extractPathParams(requestMapping.getPath(), requestURI, requestMapping.getPathPattern()));

        if(!requestMapping.isAnonimous()){
            if(!session.isPresent()) return REDIRECT_TO_LOGIN;
        }

        if (session.isPresent() && !roleService.hasAccess(requestMapping.getRoles(), session.get().getUser())) {
            return FORBIDDEN_PAGE;
        }


        if(!contentNegotiation(requestMapping, request)) return NOT_FOUND_PAGE;
        return requestMapping.getHandler();
    }

    private HashMap<String, String> extractPathParams(String path, String query, Pattern pathPattern){
        ArrayList<String> nameParams = new ArrayList<>();
        for(String part : path.split("/")){
            if(part.startsWith("{")){
                nameParams.add(part.replace("{","").replace("}",""));
            }
        }
        HashMap<String,String> params = new HashMap<>();
        Matcher matcher = pathPattern.matcher(query);
        if(matcher.find()){
            int i = 1;
            for(String name : nameParams){
                params.put(name, matcher.group(i));
                i++;
            }
        }

        return params;
    }

    private boolean contentNegotiation(RequestMapping reqMapping, HttpExchange request){
        Set<String> requestAcceptMediaTypes = getListOfRequestAcceptHeaders(request.getRequestHeaders().getFirst("Accept"));
        if(!requestAcceptMediaTypes.contains(reqMapping.getProduceMediaType().toLowerCase()) &&
                !requestAcceptMediaTypes.contains(MediaType.ALL.toLowerCase())){
            return false;
        }

        if(!reqMapping.getAcceptMediaType().equals(MediaType.ALL)){
            String requestContentType = request.getRequestHeaders().getFirst("Content-Type");
            if(requestContentType == null || !requestContentType.equals(reqMapping.getAcceptMediaType())) {
                return false;
            }
        }

        request.getResponseHeaders().add("Content-Type", reqMapping.getProduceMediaType());

        return true;
    }

    private Set<String> getListOfRequestAcceptHeaders(String requestAcceptHeader){
        if(requestAcceptHeader == null) return Collections.emptySet();
        return Stream.of(requestAcceptHeader.split(";"))
                .flatMap(header -> Stream.of(header.split(",")))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}

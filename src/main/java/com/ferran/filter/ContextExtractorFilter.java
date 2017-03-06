package com.ferran.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.Constants;
import com.ferran.http.HttpContext;
import com.ferran.http.session.Manager;
import com.ferran.http.session.Session;
import com.ferran.http.utils.HttpContextParser;
import com.ferran.http.auth.AuthStrategy;
import com.ferran.service.DateService;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.*;


public class ContextExtractorFilter<T> extends Filter {

    private Manager<T> userSessionManager;
    private DateService dateService;
    private HttpContextParser<HttpExchange> contextParser;
    private Set<AuthStrategy<T>> authStrategies = new HashSet<>();

    public ContextExtractorFilter(
            Manager<T> userSessionManager,
            DateService dateService,
            HttpContextParser<HttpExchange> contextParser,
            Set<AuthStrategy<T>> authStrategies) {
        this.userSessionManager = userSessionManager;
        this.dateService = dateService;
        this.contextParser = contextParser;
        this.authStrategies = authStrategies;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        HttpContext context = this.contextParser.createContext(httpExchange);
        httpExchange.setAttribute(Constants.REUQEST_PARAMS_KEY_NAME, context);
        Optional<Session<T>> userSession = Optional.empty();

        if(context.getAuthHeaders().isPresent()){
            String authType = context.getAuthHeaders().get().getKey();
            String authValue = context.getAuthHeaders().get().getValue();
            for(AuthStrategy<T> strategy : authStrategies){
                Optional<T> user = strategy.authenticate(authType, authValue);
                if(user.isPresent()){
                    userSession = Optional.of(userSessionManager.newSession(user.get()));
                    break;
                }
            }
            httpExchange.setAttribute(Constants.REUQEST_SESSION_KEY_NAME, userSession);
            chain.doFilter(httpExchange);
            userSession.ifPresent(
                    s -> userSessionManager.deleteSession(s)
            );
        }

        if(context.getCookies().isPresent()) {
            userSession = checkCoockies(context.getCookies().get());
        }

        httpExchange.setAttribute(Constants.REUQEST_SESSION_KEY_NAME, userSession);
        chain.doFilter(httpExchange);

    }

    private Optional<Session<T>> checkCoockies(Map<String, HttpCookie> cookies){
        Optional<Session<T>> userSession = Optional.ofNullable(cookies.get(Constants.COOKIE_TOKEN_NAME)).map(
                cookie -> userSessionManager.getSession(cookie.getValue())
        ).filter(session -> session != null);

        if(userSession.isPresent()){
            if(userSessionManager.isSessionExpired(userSession.get())){
                userSessionManager.deleteSession(userSession.get());
                userSession = Optional.empty();
            }else{
                userSessionManager.extendSession(userSession.get());
            }
        }

        return userSession;
    }

    @Override
    public String description() {
        return "Filter to extract query parameters and set login user";
    }
}

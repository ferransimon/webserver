package com.ferran.service;


import com.ferran.http.Constants;
import com.ferran.http.HttpResponse;
import com.ferran.http.auth.AuthRepository;
import com.ferran.http.session.Manager;
import com.ferran.http.session.Session;
import com.ferran.model.User;

import java.net.HttpCookie;
import java.util.Optional;

public class SessionLoginService implements LoginService<Session<User>> {


    private AuthRepository<User> authRepository;
    private Manager<User> sessionManager;

    public SessionLoginService(AuthRepository<User> authRepository, Manager<User> sessionManager) {
        this.authRepository = authRepository;
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Session<User>> doLogin(String username, String password, HttpResponse response) {
        Optional<User> user = authRepository.findByUserNameAndPassword(username, password);
        if(!user.isPresent()) return Optional.empty();
        Session<User> session = sessionManager.newSession(user.get());
        HttpCookie cookie = new HttpCookie(Constants.COOKIE_TOKEN_NAME, session.getToken());
        response.setCookie(
                cookie,
                session.getExpirationDate().toString()
        );

        return Optional.of(session);
    }

    @Override
    public void doLogout(Session<User> session) {
        sessionManager.deleteSession(session);
    }
}

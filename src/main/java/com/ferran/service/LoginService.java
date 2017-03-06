package com.ferran.service;

import com.ferran.http.HttpResponse;

import java.util.Optional;


public interface LoginService<T> {

    Optional<T> doLogin(String username, String password, HttpResponse response);
    void doLogout(T session);

}

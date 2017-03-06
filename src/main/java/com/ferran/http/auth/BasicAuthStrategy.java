package com.ferran.http.auth;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;


public class BasicAuthStrategy<T> implements AuthStrategy<T> {

    private static final String AUTH_TYPE_NAME = "Basic";
    private AuthRepository<T> authRepository;

    public BasicAuthStrategy(AuthRepository<T> authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Optional<T> authenticate(String authType, String value) {
        if(!authType.equals(AUTH_TYPE_NAME)) return Optional.empty();
        Optional<String> decoded = Optional.empty();
        try {
            decoded = Optional.of(new String(Base64.getDecoder().decode(value), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return Optional.empty();
        }

        if(decoded.isPresent()){
            String[] auth = decoded.get().split(":");
            return authRepository.findByUserNameAndPassword(auth[0], auth[1]);
        }else {
            return Optional.empty();
        }
    }
}

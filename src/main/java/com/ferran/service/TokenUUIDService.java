package com.ferran.service;

import java.util.UUID;

public class TokenUUIDService implements TokenService {

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}

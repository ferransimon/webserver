package com.ferran.http.auth;

import java.util.Optional;

public interface AuthStrategy<T> {

    Optional<T> authenticate(String authType, String value);

}

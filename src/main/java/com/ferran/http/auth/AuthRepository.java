package com.ferran.http.auth;


import java.util.Optional;

public interface AuthRepository<T> {

    Optional<T> findByUserNameAndPassword(final String username, final String password);

}

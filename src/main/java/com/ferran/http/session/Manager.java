package com.ferran.http.session;


public interface Manager<T> {

    Session<T> newSession(T user);
    Session<T> getSession(String token);
    Session<T> extendSession(Session<T> session);
    boolean isSessionExpired(Session<T> session);
    void deleteSession(Session<T> session);

}

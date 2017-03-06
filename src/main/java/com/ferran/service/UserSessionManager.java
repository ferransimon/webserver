package com.ferran.service;


import com.ferran.http.session.Manager;
import com.ferran.http.session.Session;
import com.ferran.model.User;
import com.ferran.repository.SessionInMemoryRepository;


public class UserSessionManager implements Manager<User> {

    private SessionInMemoryRepository repository;
    private ExpirationService expirationService;
    private DateService dateService;
    private TokenService tokenService;
    private final int expirationTimeInMilis;

    public UserSessionManager(SessionInMemoryRepository repository, ExpirationService expirationService, DateService dateService, TokenService tokenService, int expirationTimeInMilis) {
        this.repository = repository;
        this.expirationService = expirationService;
        this.tokenService = tokenService;
        this.dateService = dateService;
        this.expirationTimeInMilis = expirationTimeInMilis;
    }

    @Override
    public Session<User> newSession(User user) {
        Session<User> session = new Session<User>(tokenService.generateToken(), user, expirationService.getNewExpirationDate(expirationTimeInMilis));
        repository.save(session.getToken(), session);
        return session;
    }

    @Override
    public Session<User> getSession(String token) {
        return repository.get(token);
    }



    @Override
    public Session<User> extendSession(Session<User> session) {
        Session<User> savedSession = repository.get(session.getToken());
        if(savedSession != null) {
            savedSession.setExpirationDate(expirationService.getNewExpirationDate(expirationTimeInMilis));
        }
        return savedSession;
    }

    @Override
    public boolean isSessionExpired(Session<User> session) {
        return session.getExpirationDate().getTime() -
                dateService.getNow().getTime() < 0;
    }

    @Override
    public void deleteSession(Session<User> session) {
        repository.remove(session.getToken());
    }
}

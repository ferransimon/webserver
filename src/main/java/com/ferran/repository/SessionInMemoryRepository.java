package com.ferran.repository;

import com.ferran.http.session.Session;
import com.ferran.model.User;

import java.util.Map;
import java.util.Optional;


public class SessionInMemoryRepository extends AbstracMemoryRepository<String, Session<User>> {

    public SessionInMemoryRepository(Map<String, Session<User>> repository) {
        super(repository);
    }

    public Optional<Session<User>> findByUser(User user){
        return this.getAll().stream()
                .filter(s -> s.getUser().getUsername().equals(user.getUsername()))
                .findFirst();
    }
}

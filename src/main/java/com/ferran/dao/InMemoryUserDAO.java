package com.ferran.dao;


import com.ferran.model.User;
import com.ferran.repository.UserInMemoryRepository;

import java.util.List;
import java.util.Optional;

public class InMemoryUserDAO implements DAO<String, User> {

    private final UserInMemoryRepository repository;

    public InMemoryUserDAO(UserInMemoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User item) {
        repository.save(item.getUsername(), item);
        return item;
    }

    @Override
    public Optional<User> get(String id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public void remove(User user) {
        repository.remove(user.getUsername());
    }
}

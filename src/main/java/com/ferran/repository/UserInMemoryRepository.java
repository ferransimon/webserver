package com.ferran.repository;

import com.ferran.http.auth.AuthRepository;
import com.ferran.model.User;
import com.ferran.service.Encrypter;

import java.util.Map;
import java.util.Optional;


public class UserInMemoryRepository extends AbstracMemoryRepository<String, User> implements AuthRepository<User> {

    private Encrypter passwordEncrypterService;

    public UserInMemoryRepository(Map<String, User> repository, Encrypter passwordEncrypterService) {
        super(repository);
        this.passwordEncrypterService = passwordEncrypterService;
    }

    @Override
    public Optional<User> findByUserNameAndPassword(String username, String password) {
        Optional<User> user = Optional.ofNullable(this.get(username));
        return user.filter(u -> u.getPassword().equals(passwordEncrypterService.encrypt(password)));
    }
}

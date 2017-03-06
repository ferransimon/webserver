package com.ferran.service;


import com.ferran.dao.DAO;
import com.ferran.model.NewUser;
import com.ferran.model.User;
import rx.subjects.PublishSubject;

import java.util.List;
import java.util.Optional;

public class UserService{

    private DAO<String, User> userDAO;
    private Encrypter passwordEncrypterService;
    private PublishSubject<UserEvent> userUpdates;

    public UserService(DAO<String, User> userDAO, Encrypter passwordEncrypterService, PublishSubject<UserEvent> userUpdates) {
        this.userDAO = userDAO;
        this.passwordEncrypterService = passwordEncrypterService;
        this.userUpdates = userUpdates;
    }

    public Optional<User> createUser(NewUser newUser){
        if(userDAO.get(newUser.getUsername()).isPresent()){
            return Optional.empty();
        }else {
            User user = new User(
                    newUser.getUsername(),
                    passwordEncrypterService.encrypt(newUser.getPassword()),
                    newUser.getRoles()
            );
            return Optional.of(userDAO.save(user));
        }
    }

    public Optional<User> updateUser(User user){
        Optional<User> userOpt = userDAO.get(user.getUsername());
        if(userOpt.isPresent()){
            if(user.getPassword() != null) userOpt.get().setPassword(user.getPassword());
            userOpt.get().setRoles(user.getRoles());
            this.userUpdates.onNext(new UserEvent(
                    userOpt.get(),
                    UserEvent.Action.UPDATE
            ));
            return userOpt;
        }else{
            return Optional.empty();
        }
    }

    public Optional<User> deleteUser(String userId){
        Optional<User> userOpt = userDAO.get(userId);
        if(userOpt.isPresent()){
            userDAO.remove(userOpt.get());
            this.userUpdates.onNext(new UserEvent(
                    userOpt.get(),
                    UserEvent.Action.DELETE
            ));
            return userOpt;
        }else{
            return Optional.empty();
        }
    }

    public Optional<User> getUser(String username){
        return userDAO.get(username);
    }

    public List<User> getAll(){
        return userDAO.getAll();
    }
}

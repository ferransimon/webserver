package com.ferran.service;

import com.ferran.model.User;

public class UserEvent {

    public enum Action{
        UPDATE,
        DELETE
    }

    private User user;
    private Action action;

    public UserEvent(final User user, final Action action) {
        this.user = user;
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}

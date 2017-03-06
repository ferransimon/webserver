package com.ferran.service;

import com.ferran.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public class RoleService implements RoleManager<User.Role, User> {

    @Override
    public boolean hasAccess(Set<User.Role> pageRoles, User user) {
        if(pageRoles.size() == 0) return true;
        return Stream.of(pageRoles).flatMap(Collection::stream).filter(
                role -> user.getRoles().contains(role)
        ).findFirst().isPresent();
    }
}

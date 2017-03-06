package com.ferran.service;


import java.util.Set;

public interface RoleManager<T1, T2> {

    public boolean hasAccess(Set<T1> pageRoles, T2 user);

}

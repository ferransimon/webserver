package com.ferran.dao;


import java.util.List;
import java.util.Optional;

public interface DAO<S, T> {

    T save(T item);
    Optional<T> get(S id);
    List<T> getAll();
    void remove(T item);

}

package com.ferran.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstracMemoryRepository<S, T> {

    private Map<S, T> repository;

    public AbstracMemoryRepository(final Map<S, T> repository) {
        this.repository = repository;
    }

    public T save(S key, T item) {
        return repository.put(key, item);
    }

    public T get(S id) {
        return repository.get(id);
    }

    public List<T> getAll() {
        return new ArrayList<>(repository.values());
    }

    public void remove(S id) {
        repository.remove(id);
    }

}

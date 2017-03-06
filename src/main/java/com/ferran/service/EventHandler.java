package com.ferran.service;


public interface EventHandler<T> {

    void process(T event);

}

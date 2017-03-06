package com.ferran.http.routing;

import com.ferran.http.RequestMethod;

import java.util.Optional;

public interface Router<T> {

    Optional<T> getRoute(String uri, RequestMethod method);

}

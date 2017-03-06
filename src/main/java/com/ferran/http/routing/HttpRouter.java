package com.ferran.http.routing;


import com.ferran.http.RequestMethod;

import java.util.*;

public class HttpRouter implements Router<RequestMapping>{

    private Set<RequestMapping> registeredRoutes = new HashSet<>();

    public void registerRoute(RequestMapping requestMapping){
        this.registeredRoutes.add(requestMapping);
    }

    public void registerRoutes(RequestMapping... requestMappings){
        for(RequestMapping rm : requestMappings){
            this.registerRoute(rm);
        }
    }

    @Override
    public Optional<RequestMapping> getRoute(String uri, RequestMethod method) {
        return registeredRoutes.stream().filter(r -> r.match(uri))
                .filter(r -> r.getRequestMethod().contains(method))
                .sorted((r1, r2) -> r2.getPath().compareTo(r1.getPath()))
                .findFirst();

    }
}

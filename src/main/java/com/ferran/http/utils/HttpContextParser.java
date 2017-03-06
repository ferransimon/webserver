package com.ferran.http.utils;


import com.ferran.http.HttpContext;

import java.io.IOException;

public interface HttpContextParser<T> {

    HttpContext createContext(T request) throws IOException;

}

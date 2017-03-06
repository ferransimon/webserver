package com.ferran.http.render;


import java.io.IOException;
import java.util.Map;

public interface Render {

    public String render(String templateName, Map<String, String> variablesMap) throws IOException;

}

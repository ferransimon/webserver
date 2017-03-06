package com.ferran.http.render;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Pattern;

public class HtmlViewRender implements Render {

    @Override
    public String render(String templateName, Map<String, String> variablesMap) throws IOException {
        Path path;
        try {
            URL urlPath = HtmlViewRender.class.getClassLoader().getResource("views/"+templateName + ".html");
            if(urlPath == null){
                throw new IOException("File "+templateName+" not found");
            }
            path = Paths.get(urlPath.toURI());
        }catch (URISyntaxException ex){
            throw new IOException("Error reading "+templateName);
        }
        String htmlText = new String(Files.readAllBytes(path));

        if(variablesMap != null){
            for(Map.Entry<String, String> entry : variablesMap.entrySet()){
                htmlText = Pattern.compile("\\{"+entry.getKey()+"\\}").matcher(htmlText).replaceAll(entry.getValue());
            }
        }

        return htmlText;
    }
}

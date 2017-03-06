package com.ferran.http.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HtmlViewRender implements Render {

    @Override
    public String render(String templateName, Map<String, String> variablesMap) throws IOException {
        try(
            InputStream in = getClass().getClassLoader().getResourceAsStream("views/"+templateName + ".html");
            BufferedReader buff = new BufferedReader(new InputStreamReader(in))
        ){
            String line;
            List<String> responseData = new ArrayList<String>();
            while ((line = buff.readLine()) != null) {
                responseData.add(line);
            }
            String htmlText = String.join("\n", responseData);
            if(variablesMap != null){
                for(Map.Entry<String, String> entry : variablesMap.entrySet()){
                    htmlText = Pattern.compile("\\{"+entry.getKey()+"\\}").matcher(htmlText).replaceAll(entry.getValue());
                }
            }
            return htmlText;
        }
    }
}

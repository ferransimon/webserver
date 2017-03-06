package com.ferran.http;


import com.ferran.http.render.HtmlViewRender;
import com.ferran.http.render.Render;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class HtmlViewRenderTest {

    //Class under test
    private Render render = new HtmlViewRender();

    @Test
    public void test_render_simple_html_with_variables() throws IOException{
        String username = "ferran";
        String renderedHtml = "<html><body>"+username+"</body></html>";
        assertEquals(renderedHtml, render.render("page", Collections.singletonMap("username", username)));

    }

    @Test
    public void test_render_simple_html_with_multiple_variables() throws IOException{
        String username = "ferran";
        String title = "test";
        String renderedHtml = "<html><title>"+title+"</title><body>"+username+"</body></html>";
        HashMap<String, String> variables = new HashMap<>();
        variables.put("title", title);
        variables.put("username", username);
        assertEquals(renderedHtml, render.render("page2",
                variables
                ));

    }

}

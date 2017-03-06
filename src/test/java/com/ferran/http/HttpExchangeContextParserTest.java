package com.ferran.http;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.ferran.http.utils.HttpContextParser;
import com.ferran.http.utils.HttpExchangeContextParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpExchangeContextParserTest {

    //Class under test
    private HttpContextParser<HttpExchange> contextParser = new HttpExchangeContextParser();

    @Mock
    private HttpExchange mockExchange;

    @Test
    public void test_read_context() throws Exception{

        Headers headers = new Headers();
        headers.add("Authorization", "Bearer afafafafafafaffaf");
        headers.add("Cookie", "token=fb1cb7ce-04d5-4d5a-85b6-ec2ce7196be7");

        when(mockExchange.getRequestHeaders()).thenReturn(
                headers
        );
        InputStream stream = new ByteArrayInputStream("user=test&password=1234".getBytes(StandardCharsets.UTF_8));
        when(mockExchange.getRequestBody()).thenReturn(
                new BufferedInputStream(stream)
        );
        when(mockExchange.getRequestURI()).thenReturn(
                new URI("http",null,"localhost",8080, "/login", "redirect=/home", null)
        );

        HttpContext context = contextParser.createContext(mockExchange);

        assertEquals("test", context.getFormParams().get().get("user"));
        assertEquals("1234", context.getFormParams().get().get("password"));
        assertEquals("/home", context.getQueryParams().get().get("redirect"));
        assertEquals("afafafafafafaffaf", context.getAuthHeaders().get().getValue());
        assertEquals("Bearer", context.getAuthHeaders().get().getKey());
        assertEquals("fb1cb7ce-04d5-4d5a-85b6-ec2ce7196be7", context.getCookies().get().get("token").getValue());
    }

}

package com.ferran;

import com.sun.net.httpserver.HttpExchange;
import com.ferran.controller.LoginController;
import com.ferran.http.*;
import com.ferran.http.render.Render;
import com.ferran.http.routing.RequestHandler;
import com.ferran.http.session.Session;
import com.ferran.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.ferran.service.LoginService;

import java.io.IOException;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    private Render render;
    @Mock
    private LoginService<Session<User>> loginService;

    @Mock
    private HttpExchange mockExchange;

    @Mock
    private HttpResponse mockResponse;

    //Class under test
    private RequestHandler loginController;

    @Before
    public void setUp() throws IOException{
        when(loginService.doLogin(any(),any(), (HttpResponse)any())).thenReturn(
                Optional.of(new Session<User>("test", new User("test", "test", Collections.emptySet()), new Date()))
        );
        when(render.render(any(), any())).thenReturn("test");
        reset(mockExchange, loginService, render);
        loginController =  new LoginController(render, loginService);
    }

    @Test
    public void test_get_login_page() throws IOException{

        when(mockExchange.getAttribute(Constants.REUQEST_PARAMS_KEY_NAME)).thenReturn(
                new HttpContext()
        );

        when(mockExchange.getAttribute(Constants.REUQEST_SESSION_KEY_NAME)).thenReturn(
                Optional.empty()
        );

        when(mockExchange.getRequestMethod()).thenReturn(RequestMethod.GET.name());

        loginController.handle(mockExchange, mockResponse);
        verify(mockResponse, times(1)).sendResponse(any());
    }

    @Test
    public void test_post_login() throws IOException{
        HttpContext context = new HttpContext();
        HashMap<String, String> formParams = new HashMap<>();
        formParams.put("username", "test");
        formParams.put("password", "test");
        context.setFormParams(
                formParams
        );
        when(mockExchange.getAttribute(Constants.REUQEST_PARAMS_KEY_NAME)).thenReturn(
                context
        );

        when(loginService.doLogin("test", "test", mockResponse)).thenReturn(
                Optional.of(new Session<User>("test", new User("test", "test", Collections.emptySet()), new Date()))
        );

        when(mockExchange.getAttribute(Constants.REUQEST_SESSION_KEY_NAME)).thenReturn(
                Optional.empty()
        );

        when(mockExchange.getRequestMethod()).thenReturn(RequestMethod.POST.name());

        loginController.handle(mockExchange, mockResponse);
        verify(loginService, times(1)).doLogin("test", "test", mockResponse);
        verify(mockResponse, times(2)).redirect(any());
    }

}

package com.ferran;

import com.ferran.controller.LoginController;
import com.ferran.controller.PageController;
import com.ferran.controller.rest.*;
import com.ferran.http.MediaType;
import com.ferran.http.RequestMethod;
import com.ferran.http.render.Render;
import com.ferran.http.routing.RequestHandler;
import com.ferran.http.routing.RequestMapping;
import com.ferran.http.session.Session;
import com.ferran.http.tokenizer.Tokenizer;
import com.ferran.model.User;
import com.ferran.service.LoginService;
import com.ferran.service.UserService;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Routes{

    final RequestMapping login;
    final RequestMapping logout;
    final RequestMapping homePage;
    final RequestMapping page1;
    final RequestMapping page2;
    final RequestMapping page3;
    final RequestMapping usersList;
    final RequestMapping usersPost;
    final RequestMapping usersDelete;
    final RequestMapping usersById;
    final RequestMapping usersPut;

    public Routes(
            final Gson gson,
            final Render htmlViewRender,
            final LoginService<Session> loginService,
            final UserService userService,
            Tokenizer tokenizerService) {
        RequestHandler loginController = new LoginController(htmlViewRender, loginService);
        RequestHandler logoutController = new LoginController(htmlViewRender, loginService);
        RequestHandler homeController = new PageController("home", htmlViewRender);
        RequestHandler userListController = new ListUsersController(gson, userService);
        RequestHandler userPostController = new CreateUserController(gson, userService);
        RequestHandler userDeleteController = new DeleteUserController(gson, userService);
        RequestHandler userByIdController = new GetUserByIdController(gson, userService);
        RequestHandler updateUsersController = new UpdateUserController(gson, userService);

        login = new RequestMapping.RequestMappingBuilder(
                "/login",
                Arrays.asList(RequestMethod.GET, RequestMethod.POST),
                loginController,
                tokenizerService)
                .isAnonimous(true)
                .build();


        logout = new RequestMapping.RequestMappingBuilder(
                "/logout",
                Collections.singletonList(RequestMethod.GET),
                logoutController,
                tokenizerService)
                .build();


        page1 = new RequestMapping.RequestMappingBuilder(
                "/page1",
                Collections.singletonList(RequestMethod.GET),
                new PageController("page1", htmlViewRender),
                tokenizerService)
                .withRoles(Collections.singleton(User.Role.PAGE_1))
                .build();

        page2 = new RequestMapping.RequestMappingBuilder(
                "/page2",
                Collections.singletonList(RequestMethod.GET),
                new PageController("page2", htmlViewRender),
                tokenizerService)
                .withRoles(Collections.singleton(User.Role.PAGE_2))
                .build();

        page3 = new RequestMapping.RequestMappingBuilder(
                "/page3",
                Collections.singletonList(RequestMethod.GET),
                new PageController("page3", htmlViewRender),
                tokenizerService)
                .withRoles(Collections.singleton(User.Role.PAGE_3))
                .build();

        usersList = new RequestMapping.RequestMappingBuilder(
                "/api/users",
                Collections.singletonList(RequestMethod.GET),
                userListController,
                tokenizerService)
                .withProduceMediaType(MediaType.APPLICATION_JSON)
                .withRoles(new HashSet<>(Arrays.asList(
                        User.Role.PAGE_1, User.Role.PAGE_2, User.Role.PAGE_3, User.Role.ADMIN)))
                .build();

        usersPost = new RequestMapping.RequestMappingBuilder(
                "/api/users",
                Collections.singletonList(RequestMethod.POST),
                userPostController,
                tokenizerService)
                .withProduceMediaType(MediaType.APPLICATION_JSON)
                .withAcceptMediaType(MediaType.APPLICATION_JSON)
                .withRoles(Collections.singleton(User.Role.ADMIN))
                .build();

        usersPut = new RequestMapping.RequestMappingBuilder(
                "/api/users",
                Collections.singletonList(RequestMethod.PUT),
                updateUsersController,
                tokenizerService)
                .withProduceMediaType(MediaType.APPLICATION_JSON)
                .withAcceptMediaType(MediaType.APPLICATION_JSON)
                .withRoles(Collections.singleton(User.Role.ADMIN))
                .build();

        usersDelete = new RequestMapping.RequestMappingBuilder(
                "/api/users/{userId}",
                Collections.singletonList(RequestMethod.DELETE),
                userDeleteController,
                tokenizerService)
                .withProduceMediaType(MediaType.APPLICATION_JSON)
                .withRoles(Collections.singleton(User.Role.ADMIN))
                .build();

        usersById = new RequestMapping.RequestMappingBuilder(
                "/api/users/{userId}",
                Collections.singletonList(RequestMethod.GET),
                userByIdController,
                tokenizerService)
                .withProduceMediaType(MediaType.APPLICATION_JSON)
                .withRoles(new HashSet<>(Arrays.asList(
                        User.Role.PAGE_1, User.Role.PAGE_2, User.Role.PAGE_3, User.Role.ADMIN)))
                .build();

        homePage = new RequestMapping.RequestMappingBuilder(
                "/home",
                Collections.singletonList(RequestMethod.GET),
                homeController,
                tokenizerService)
                .build();

    }


}

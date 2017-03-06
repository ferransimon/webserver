package com.ferran;

import com.ferran.dao.DAO;
import com.ferran.dao.InMemoryUserDAO;
import com.ferran.filter.ContextExtractorFilter;
import com.ferran.http.auth.AuthStrategy;
import com.ferran.http.auth.BasicAuthStrategy;
import com.ferran.http.render.HtmlViewRender;
import com.ferran.http.render.Render;
import com.ferran.http.routing.HttpRouter;
import com.ferran.http.session.Manager;
import com.ferran.http.session.Session;
import com.ferran.http.tokenizer.BasicPathTokenizer;
import com.ferran.http.tokenizer.Tokenizer;
import com.ferran.http.utils.HttpContextParser;
import com.ferran.http.utils.HttpExchangeContextParser;
import com.ferran.model.NewUser;
import com.ferran.model.User;
import com.ferran.repository.SessionInMemoryRepository;
import com.ferran.repository.UserInMemoryRepository;
import com.ferran.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class WebApp {

    private final static Logger LOGGER = Logger.getLogger(WebApp.class.getName());
    private final static int EXPIRATION_SESSION_TIME = 5*60*1000;

    public static void main(String[] args) throws IOException{

        /**
         * Render services used by Controllers
         */
        Gson gson = new GsonBuilder().create();
        Render htmlViewRender = new HtmlViewRender();

        /**
         * Utility Services
         */
        Encrypter encrypter = new Sha1EncrypterService();
        TokenService tokenService = new TokenUUIDService();
        ExpirationService expirationService = new GregorianCalExpirationService();
        HttpContextParser<HttpExchange> contextParser = new HttpExchangeContextParser();
        Tokenizer tokenizer = new BasicPathTokenizer();
        DateService dateService = new DateService();

        /**
         * Repositories and DAO services
         */
        UserInMemoryRepository userInMemoryRepository = new UserInMemoryRepository(
                new ConcurrentHashMap<String, User>(),
                encrypter
        );
        DAO<String, User> userDAO = new InMemoryUserDAO(userInMemoryRepository);
        SessionInMemoryRepository sessionRepository = new SessionInMemoryRepository(
                new ConcurrentHashMap<String, Session<User>>()
        );

        /**
         * Services used by the app
         */
        Manager<User> sessionManager = new UserSessionManager(
                sessionRepository,
                expirationService,
                dateService,
                tokenService,
                EXPIRATION_SESSION_TIME
        );

        /**
         * Creating the subject that will handler user actions, like update or delete
         * to update the session and log the action performed. Note how easy would be to
         * add a handler to send statistically data to a external service for interactions with the API.
         */
        PublishSubject<UserEvent> userUpdates = PublishSubject.create();
        EventHandler<UserEvent> userEventHandler = new UserEventHandler(sessionRepository);
        userUpdates.subscribe(userEventHandler::process);
        userUpdates.subscribe(
                event -> {
                    LOGGER.info("User "+event.getUser().getUsername()+" event "+event.getAction().name());
                }
        );

        UserService userService = new UserService(userDAO, encrypter, userUpdates);
        LoginService<Session<User>> loginService = new SessionLoginService(userInMemoryRepository, sessionManager);
        AuthStrategy<User> basicAuth = new BasicAuthStrategy<>(userInMemoryRepository);
        RoleManager<User.Role, User> roleService = new RoleService();


        /**
         * Create 3 initial Users
         */
        userService.createUser(
                new NewUser("user1", "password", Collections.singleton(User.Role.PAGE_1))
        );

        userService.createUser(
                new NewUser("user2", "password", Collections.singleton(User.Role.PAGE_2))
        );

        userService.createUser(
                new NewUser("user3", "password", Collections.singleton(User.Role.PAGE_3))
        );

        userService.createUser(
                new NewUser("admin", "admin", Collections.singleton(User.Role.ADMIN))
        );

        /**
         * Init server
         */
        HttpRouter router = new HttpRouter();
        Routes routes = new Routes(
                gson,
                htmlViewRender,
                loginService,
                userService,
                tokenizer
        );

        router.registerRoutes(
                routes.homePage,
                routes.login,
                routes.logout,
                routes.page1,
                routes.page2,
                routes.page3,
                routes.usersById,
                routes.usersDelete,
                routes.usersList,
                routes.usersPost,
                routes.usersPut
        );

        HttpGateway gateway = new HttpGateway(router, roleService);
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = httpServer.createContext("/", gateway);
        context.getFilters().add(new ContextExtractorFilter<User>(
                sessionManager,
                contextParser,
                Collections.singleton(basicAuth)
            )
        );
        httpServer.setExecutor(null);
        LOGGER.info("Starting Server");
        httpServer.start();
    }


}

package ru.otus.server;

import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.ServiceUserDao;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.*;

import com.google.gson.Gson;
import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

public class UsersWebServerFormAuthentication implements UsersWebServer {
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final AbstractLoginService loginService;
    private final ServiceUserDao serviceUserDao;
    private final Gson gson;
    private final Server server;

    protected final TemplateProcessor templateProcessor;

    public UsersWebServerFormAuthentication(int port,
                                            AbstractLoginService loginService,
                                            ServiceUserDao serviceUserDao,
                                            Gson gson,
                                            TemplateProcessor templateProcessor) {
        this.loginService = loginService;
        this.serviceUserDao = serviceUserDao;
        this.gson = gson;
        this.templateProcessor = templateProcessor;

        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();
        SecurityHandler securityHandler = createSecurityHandler(new String[]{"/admins", "/api/admins/*"}, new String[]{"/users", "/api/users/*"});

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        servletContextHandler.setSecurityHandler(securityHandler);
        handlers.addHandler(servletContextHandler);

        server.setHandler(handlers);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));

        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);

        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, serviceUserDao)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(serviceUserDao, gson)), "/api/user/*");
        servletContextHandler.addServlet(new ServletHolder(new AdminsServlet(templateProcessor, serviceUserDao)), "/admins");
        servletContextHandler.addServlet(new ServletHolder(new AdminsApiServlet(serviceUserDao, gson)), "/api/admin/*");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor)), "/login");

        return servletContextHandler;
    }

    private SecurityHandler createSecurityHandler(String[] adminPaths, String[] userPaths) {
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

        Constraint usersConstraint = new Constraint();
        usersConstraint.setName("USERS_CONSTRAINT");
        usersConstraint.setRoles(new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN});
        usersConstraint.setAuthenticate(true);

        for (String path : userPaths) {
            ConstraintMapping constraintMapping = new ConstraintMapping();
            constraintMapping.setConstraint(usersConstraint);
            constraintMapping.setPathSpec(path);
            securityHandler.addConstraintMapping(constraintMapping);
        }

        Constraint adminConstraint = new Constraint();
        adminConstraint.setName("ADMINS_CONSTRAINT");
        adminConstraint.setRoles(new String[]{ROLE_NAME_ADMIN});
        adminConstraint.setAuthenticate(true);

        for (String path : adminPaths) {
            ConstraintMapping constraintMapping = new ConstraintMapping();
            constraintMapping.setConstraint(adminConstraint);
            constraintMapping.setPathSpec(path);
            securityHandler.addConstraintMapping(constraintMapping);
        }

        securityHandler.setLoginService(loginService);

        securityHandler.setAuthenticator(new FormAuthenticator("/login", "/login", false));

        return securityHandler;
    }
}

package ru.otus;

import ru.otus.hibernate.HibernateUtils;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerFormAuthentication;
import ru.otus.hibernate.services.HibernateLoginServiceImpl;
import ru.otus.services.ServiceUserDao;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.AbstractLoginService;

public class WebServerFormAuthentication {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        ServiceUserDao serviceUserDao = HibernateUtils.createServiceUserDao(HIBERNATE_CFG_FILE);

        AbstractLoginService loginService = new HibernateLoginServiceImpl(serviceUserDao);

        UsersWebServer usersWebServer = new UsersWebServerFormAuthentication(
                WEB_SERVER_PORT,
                loginService,
                serviceUserDao,
                gson,
                templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}

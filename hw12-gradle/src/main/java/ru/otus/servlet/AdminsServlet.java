package ru.otus.servlet;

import ru.otus.services.ServiceUserDao;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminsServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "admins.html";
    private static final String TEMPLATE_ATTR_ALL_USERS = "allUsers";
    private static final String CONTENT_TYPE = "text/html";

    private final ServiceUserDao serviceUserDao;
    private final TemplateProcessor templateProcessor;

    public AdminsServlet(TemplateProcessor templateProcessor, ServiceUserDao serviceUserDao) {
        this.templateProcessor = templateProcessor;
        this.serviceUserDao = serviceUserDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_ALL_USERS, serviceUserDao.findAllUsers());

        response.setContentType(CONTENT_TYPE);
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}

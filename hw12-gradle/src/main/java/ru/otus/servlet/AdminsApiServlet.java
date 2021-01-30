package ru.otus.servlet;

import ru.otus.model.User;
import ru.otus.services.ServiceUserDao;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminsApiServlet extends HttpServlet {
    private final ServiceUserDao serviceUserDao;

    public AdminsApiServlet(ServiceUserDao serviceUserDao) {
        this.serviceUserDao = serviceUserDao;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String[] roles = request.getParameter("roles").replaceAll("\\s+", "").split(",");

        serviceUserDao.saveUser(new User(name, login, password, roles));

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{}");
    }
}

package ru.otus.servlet;

import ru.otus.model.User;
import ru.otus.services.ServiceUserDao;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.google.gson.Gson;

public class UsersApiServlet extends HttpServlet {
    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private final ServiceUserDao serviceUserDao;
    private final Gson gson;

    public UsersApiServlet(ServiceUserDao serviceUserDao, Gson gson) {
        this.serviceUserDao = serviceUserDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = serviceUserDao.findById(extractIdFromRequest(request)).orElse(null);

        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
package ru.otus.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import ru.otus.model.User;
import ru.otus.services.ServiceUserDao;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class AdminsApiServlet extends HttpServlet {
    private final ServiceUserDao serviceUserDao;
    private final Gson gson;

    public AdminsApiServlet(ServiceUserDao serviceUserDao, Gson gson) {
        this.serviceUserDao = serviceUserDao;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        GsonBuilder gsonBuilder = gson.newBuilder();
        gsonBuilder.registerTypeAdapter(User.class, (JsonDeserializer<User>) (jElement, typeOfT, context) -> {
            JsonObject jObject = jElement.getAsJsonObject();
            String nameValue = jObject.get("name").getAsString();
            String loginValue = jObject.get("login").getAsString();
            String passwordValue = jObject.get("password").getAsString();
            String rolesValue = jObject.get("roles").getAsString();

            String[] arrRoles = rolesValue.replaceAll("\\s+", "").split(",");
            return new User(nameValue, loginValue, passwordValue, arrRoles);
        });
        User targetObject = gsonBuilder.create().fromJson(body, User.class);

        serviceUserDao.saveUser(targetObject);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().println("{}");
    }
}

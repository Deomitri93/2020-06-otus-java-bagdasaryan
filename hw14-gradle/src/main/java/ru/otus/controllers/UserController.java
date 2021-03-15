package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.domain.User;
import ru.otus.services.UsersService;

import java.util.List;
import java.util.Random;

@Controller
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping({"/", "/index"})
    public String indexView(Model model) {
        return "index.html";
    }

    @GetMapping("/users")
    public String usersView(Model model) {
        List<User> users = usersService.findAll();
        Random random = new Random();
        model.addAttribute("randomUser", users.get(random.nextInt(users.size())));
        return "users.html";
    }

    @GetMapping("/userList")
    public String userListView(Model model) {
        List<User> users = usersService.findAll();
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping("/admins")
    public String adminsView(Model model) {
        List<User> users = usersService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        return "admins.html";
    }

//    @GetMapping("/user/create")
//    public String userCreateView(Model model) {
//        model.addAttribute("user", new User());
//        return "userCreate.html";
//    }


    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        usersService.save(user);
        return new RedirectView("/", true);
    }

}

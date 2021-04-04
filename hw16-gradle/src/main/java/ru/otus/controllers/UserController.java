package ru.otus.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.HtmlUtils;
import ru.otus.domain.Message;
import ru.otus.domain.User;
import ru.otus.domain.UserDTO;
import ru.otus.services.UsersService;

import java.util.List;
import java.util.Random;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final SimpMessagingTemplate template;
    private final Gson gson;

//    @Autowired
    private UsersService usersService;

    public UserController(SimpMessagingTemplate template, UsersService usersService, Gson gson) {
        this.template = template;
        this.usersService = usersService;
        this.gson = gson;
    }

//    @GetMapping({"/", "/index"})
//    public String indexView(Model model) {
//        return "index.html";
//    }

//    @GetMapping("/users")
//    public String usersView(Model model) {
//        List<User> users = usersService.findAll();
//        Random random = new Random();
//        model.addAttribute("randomUser", users.get(random.nextInt(users.size())));
//        return "users.html";
//    }

//    @GetMapping("/userList")
//    public String userListView(Model model) {
//        List<User> users = usersService.findAll();
//        model.addAttribute("users", users);
//        return "userList.html";
//    }

//    @GetMapping("/admins")
//    public String adminsView(Model model) {
//        List<User> users = usersService.findAll();
//        model.addAttribute("users", users);
//        model.addAttribute("newUser", new UserDTO());
//        return "admins.html";
//    }

//    @PostMapping("/user/save")
//    public RedirectView userSave(@ModelAttribute UserDTO newUser) {
//        usersService.save(new User(newUser));
//        return new RedirectView("/admins", true);
//    }

    @MessageMapping("/getRandomUser.{uuid}")
//    @SendTo("/topic/randomUser.{uuid}")
    public void getRandomUser(@DestinationVariable String uuid) {
        logger.info("got message from front: {}, uuid: {}", "getRandomUser", uuid);

        List<User> users = usersService.findAll();
        Random random = new Random();
//        return new User(users.get(random.nextInt(users.size())));

//        private void sentMessage(String description, Object data){
        template.convertAndSend("/topic/randomUser." + uuid, gson.toJson(new User(users.get(random.nextInt(users.size())))));
//        }
    }

//    @MessageMapping("/getAllUsers.{uuid}")
//    @SendTo("/topic/allUsers.{uuid}")
//    public List<User> getUsers(@DestinationVariable String uuid) {
//        logger.info("/getAllUsers.{}", uuid);
//
//        return usersService.findAll();
//    }
}
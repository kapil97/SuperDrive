package com.udacity.jwdnd.course1.fileManager.controllers;

import com.udacity.jwdnd.course1.fileManager.model.User;
import com.udacity.jwdnd.course1.fileManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getSignupPage(User user, Model model){
        System.out.println("Get method of user Signup");
        return "signup";
    }
    @PostMapping()
    public String onSubmit(User user, Model model){
        String error = null;
        if (!userService.isUsernameAvailable(user.getUsername())) {
            error = "The username already exists.";
        }

        if (error == null) {
            int rowsAdded = userService.createUser(user);
            if(rowsAdded>0) {
//                System.out.println("User has created with user name: " + userService.isUsernameAvailable(user.getUsername()));
                System.out.println("User Details: "+ userService.getAllUserData());
            }
            if (rowsAdded < 0) {
                System.out.println("User Not Created");
                error = "There was an error signing you up. Please try again.";
            }
        }

        if (error == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", error);
        }

        return "signup";
    }
}

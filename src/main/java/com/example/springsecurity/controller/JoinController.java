package com.example.springsecurity.controller;

import com.example.springsecurity.dto.JoinDTO;
import com.example.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    private final UserService userService;

    @Autowired
    public JoinController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/join")
    public String joinP() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(@ModelAttribute JoinDTO joinDTO) {
        userService.joinProcess(joinDTO);
        return "redirect:/login";
    }
}

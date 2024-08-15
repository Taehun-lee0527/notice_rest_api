package com.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {

    @GetMapping({"/", "/index"})
    public String test(
    ) throws Exception{
        return "index";
    }

    @GetMapping("/join")
    public String join(
    ) throws Exception{
        return "join";
    }

    @GetMapping("/login")
    public String login(
    ) throws Exception{
        return "login";
    }

    @GetMapping("/home")
    public String home(
    ) throws Exception{
        return "home";
    }
}

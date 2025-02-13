package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
@Controller

public class FirstController {
@GetMapping("/hi")
    public String niceToMeetYou(Model model) {
    model.addAttribute("username", "Mingyu Lee");
    return "greetings"; // returns greetings.mustache object.
    }

@GetMapping("/bye")
    public String seeYouNext(Model model) {
        model.addAttribute("nickname", "Mingyu Lee");
        return "goodbye"; // returns goodbye.mustache object.
    }
}



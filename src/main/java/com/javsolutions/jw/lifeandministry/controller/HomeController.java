package com.javsolutions.jw.lifeandministry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for handling home page and hello world example requests
 */
@Controller
public class HomeController {

    /**
     * Handles the root path request and serves the hello world page
     * 
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Life and Ministry Application!");
        model.addAttribute("title", "Home");
        return "home";
    }
    
    /**
     * Explicit hello world mapping
     * 
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/hello")
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello, World!");
        model.addAttribute("title", "Hello World Example");
        return "hello";
    }
}

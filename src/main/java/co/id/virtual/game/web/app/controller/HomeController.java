package co.id.virtual.game.web.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public String home() {
        return "lobby";
    }
    
    @GetMapping("/test")
    @PreAuthorize("permitAll()")
    public String test() {
        return "lobby";
    }
}
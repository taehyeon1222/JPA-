package nth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    @GetMapping("/")
    public String root(){
        return "redirect:/home";
    }

    @GetMapping("/layout")
    public String root1(){
        return "layout/navbar";
    }




}

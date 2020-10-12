package net.proselyte.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuccessController {
    @GetMapping("/auth/success")
    public String getSuccessPage(){
        return "success";
    }
}

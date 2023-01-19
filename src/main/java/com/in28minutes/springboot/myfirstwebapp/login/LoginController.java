package com.in28minutes.springboot.myfirstwebapp.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("login")
    public String gotoLoginPage(@RequestParam String name, ModelMap model){
        System.out.println("Request param is " +name);
        model.put("name",name);
        return "login";
    }

}

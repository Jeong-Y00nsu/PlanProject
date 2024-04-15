package com.jRyun.demo.planProject.user.controller;

import com.jRyun.demo.planProject.user.domain.User;
import com.jRyun.demo.planProject.user.service.UserService;
import com.jRyun.demo.planProject.util.Response;
import com.jRyun.demo.planProject.util.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;

    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/signInPage")
    public String signInPage(Model model){
        return "user/signIn";
    }

    @RequestMapping("/signIn")
    public String signIn(HttpServletRequest httpServletRequest, Model model, @RequestParam(name="user")User user){
        Response response = userService.signIn(user);
        if(response.getResult()== ResultCode.OK){
            httpServletRequest.getSession().invalidate();
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("userId", user.getId());
            return "plan/getMonthlyPlan";
        }else {
            model.addAttribute("result",response.getMessage());
            return "user/signIn";
        }
    }

    @RequestMapping("/signUpPage")
    public String signUpPage(Model model){
        return "user/signUp";
    }

    @RequestMapping("/signUp")
    public String signUp(Model model, @RequestParam(name="user")User user){
        Response response = userService.signUp(user);
        if(response.getResult()== ResultCode.OK){
            return "user/signIn";
        }else {
            model.addAttribute("result",response.getMessage());
            return "user/signUp";
        }
    }

    @RequestMapping("/signOut")
    public String signUp(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "user/signIn";
    }
}
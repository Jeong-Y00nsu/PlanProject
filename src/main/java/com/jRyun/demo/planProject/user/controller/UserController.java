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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/signInPage")
    public String signInPage(Model model){
        return "/user/signIn";
    }

    @RequestMapping("/signIn")
    public String signIn(HttpServletRequest httpServletRequest, Model model, @RequestParam(name="user")User user){
        logger.info("[UserController.signIn()] signIn start.");
        Response response = userService.signIn(user);
        if(response.getResult()== ResultCode.OK){
            httpServletRequest.getSession().invalidate();
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("userId", user.getId());
            logger.info("[UserController.signIn()] signIn success.");
            return "/plan/getMonthlyPlan";
        }else {
            model.addAttribute("result",response.getMessage());
            logger.info("[UserController.signIn()] signIn failed.");
            return "/user/signIn";
        }
    }

    @RequestMapping("/signUpPage")
    public String signUpPage(Model model){
        return "/user/signUp";
    }

    @PostMapping("/signUp")
    public String signUp(Model model, @RequestParam(name="user")User user){
        Response response = userService.signUp(user);
        if(response.getResult()== ResultCode.OK){
            return "/user/signIn";
        }else {
            model.addAttribute("result",response.getMessage());
            return "/user/signUp";
        }
    }

    @RequestMapping("/signOut")
    public String signUp(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "/user/signIn";
    }

    @PostMapping("/checkDuplicateId")
    public ResponseEntity<Response> checkDuplicateId (@RequestBody(required = false) Map<String, Object> userId){
        if (userId == null) {
            return ResponseEntity.badRequest().body(new Response(ResultCode.FAIL, "User data is null"));
        }
        String id = (String)userId.get("id");
        Response response = userService.isDuplicatedId(id);
        if (response == null) {
            return ResponseEntity.internalServerError().body(new Response(ResultCode.FAIL, "Internal server error"));
        }
        return ResponseEntity.ok(response);
    }
}

package com.jRyun.demo.planProject.plan.controller;

import com.jRyun.demo.planProject.plan.domain.Plan;
import com.jRyun.demo.planProject.plan.domain.PlanReq;
import com.jRyun.demo.planProject.plan.service.PlanService;
import com.jRyun.demo.planProject.util.Response;
import com.jRyun.demo.planProject.util.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService){
        this.planService = planService;
    }

    static final Logger logger = LoggerFactory.getLogger(PlanController.class);
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddhh24mm");

    @RequestMapping("/getMonthlyPlan")
    public String getMonthlyPlan(Model model, @RequestParam(name="planReq", required = false)PlanReq planReq, @SessionAttribute(name="userId")String userId){
        logger.info("[PlanController.getMonthlyPlan()] planReq={}, userId={}",planReq.toString(),userId);
        List<Map<LocalDate, List<Plan>>> monthlyPlan;
        if(planReq==null) {
            LocalDate localDate = LocalDate.now();
            monthlyPlan = planService.getMonthlyPlan(String.valueOf(localDate.getYear()),String.valueOf(localDate.getMonth()),userId);
        }else {
            monthlyPlan = planService.getMonthlyPlan(String.valueOf(planReq.getYear()),String.valueOf(planReq.getMonth()),userId);
        }
        model.addAttribute("monthlyPlan",monthlyPlan);
        logger.info("[PlanController.getMonthlyPlan()] response");
        return "/plan/getMonthlyPlan";
    }

    @RequestMapping("/getDailyPlan")
    public String getDailyPlan(Model model, @RequestParam("planReq") PlanReq planReq, @SessionAttribute(name="userId")String userId){
        logger.info("[PlanController.getDailyPlan()] planReq={}, userId={}",planReq.toString(), userId);
        List<Plan> dailyPlan = planService.getDailyPlan(String.valueOf(planReq.getYear()),String.valueOf(planReq.getMonth()),String.valueOf(planReq.getDay()),userId);
        model.addAttribute("dailyPlan",dailyPlan);
        logger.info("[PlanController.getDailyPlan()] response");
        return "/plan/getDailyPlan";
    }

    @RequestMapping("/getPlan")
    public String getPlan(Model model, @RequestParam("id") String id){
        logger.info("[PlanController.getPlan()] planId={}",id);
        model.addAttribute("plan",planService.getPlanById(id));
        logger.info("[PlanController.getPlan()] response");
        return "/plan/getPlan";
    }

    @RequestMapping("/addPage")
    public String addPage(){
        return "plan/addPlan";
    }

    @RequestMapping("/addPlan")
    public String addPlan(Model model, @RequestParam("planReq") PlanReq planReq, @SessionAttribute(name="userId")String userId){
        logger.info("[PlanController.addPlan()] planReq={}, userId={}",planReq.toString(), userId);
        Plan plan = new Plan(planReq.getTitle(),planReq.getText(),LocalDate.parse(planReq.getStartDtStr(),dtf),LocalDate.parse(planReq.getEndDtStr(),dtf),userId);

        Response response = planService.addPlan(plan);
        if(response.getResult().equals(ResultCode.OK)){
            logger.info("[PlanController.addPlan()] Add plan success");
            return "/plan/getMonthlyPlan";
        } else if(response.getResult().equals(ResultCode.INVALID_PARAM)){
            model.addAttribute("result",response.getResult());
            logger.info("[PlanController.addPlan()] Invalid input param");
            return "/plan/addPlan";
        }else{
            model.addAttribute("result","다시 시도해주세요.");
            logger.info("[PlanController.addPlan()] Error");
            return "/plan/addPlan";
        }
    }

    @RequestMapping("/modifyPage")
    public String modifyPage(Model model, @RequestParam("id") String id) {
        logger.info("[PlanController.modifyPage()] planId={}",id);
        Plan plan = planService.getPlanById(id);
        model.addAttribute("plan",plan);
        logger.info("[PlanController.modifyPage()] response");
        return "/plan/modifyPlan";
    }

    @RequestMapping("/modifyPlan")
    public String modifyPlan(Model model, @RequestParam("planReq") PlanReq planReq){
        logger.info("[PlanController.modifyPlan()] planReq={}",planReq.toString());
        Plan plan = new Plan(planReq.getId(),planReq.getTitle(),planReq.getText(),LocalDate.parse(planReq.getStartDtStr(),dtf),LocalDate.parse(planReq.getEndDtStr(),dtf));

        Response response = planService.updatePlan(plan);
        if(response.getResult().equals(ResultCode.OK)){
            logger.info("[PlanController.modifyPlan()] Modify plan success");
            return "/plan/getMonthlyPlan";
        } else if(response.getResult().equals(ResultCode.INVALID_PARAM)){
            model.addAttribute("result",response.getResult());
            logger.info("[PlanController.modifyPlan()] Invalid input param");
            return "/plan/modifyPlan";
        }else{
            model.addAttribute("result","다시 시도해주세요.");
            logger.info("[PlanController.modifyPlan()] Error");
            return "/plan/modifyPlan";
        }
    }

    @RequestMapping("/deletePlan")
    public String deletePlan(@RequestParam("planId")String planId){
        logger.info("[PlanController.deletePlan()] planId={}",planId);
        planService.deletePlan(planId);
        return "/plan/getMonthlyPlan";
    }
}

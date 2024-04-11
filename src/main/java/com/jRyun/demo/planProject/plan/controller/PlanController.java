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

    private PlanService planService;

    @Autowired
    public PlanController(PlanService planService){
        this.planService = planService;
    }

    static final Logger logger = LoggerFactory.getLogger(PlanController.class);
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddhh24mm");

    @RequestMapping("/getMonthlyPlan")
    public String getMonthlyPlan(Model model, @RequestParam(name="planReq", required = false)PlanReq planReq){
        List<Map<LocalDate, List<Plan>>> monthlyPlan;
        if(planReq==null) {
            LocalDate localDate = LocalDate.now();
            monthlyPlan = planService.getMonthlyPlan(String.valueOf(localDate.getYear()),String.valueOf(localDate.getMonth()));
        }else {
            monthlyPlan = planService.getMonthlyPlan(String.valueOf(planReq.getYear()),String.valueOf(planReq.getMonth()));
        }
        model.addAttribute("monthlyPlan",monthlyPlan);
        return "plan/getMonthlyPlan";
    }

    @RequestMapping("/getDailyPlan")
    public String getDailyPlan(Model model, @RequestParam("planReq") PlanReq planReq){
        List<Plan> dailyPlan = planService.getDailyPlan(String.valueOf(planReq.getYear()),String.valueOf(planReq.getMonth()),String.valueOf(planReq.getDay()));
        model.addAttribute("dailyPlan",dailyPlan);
        return "plan/getDailyPlan";
    }

    @RequestMapping("/getPlan")
    public String getPlan(Model model, @RequestParam("planId") String planId){
        model.addAttribute("plan",planService.getPlanById(planId));
        return "plan/getPlan";
    }

    @RequestMapping("/addPage")
    public String addPage(Model model){
        return "plan/addPlan";
    }

    @RequestMapping("/addPlan")
    public String addPlan(Model model, @RequestParam("planReq") PlanReq planReq, @SessionAttribute(name="userId")String userId){

        Plan plan = new Plan(planReq.getTitle(),planReq.getText(),LocalDate.parse(planReq.getStartDtStr(),dtf),LocalDate.parse(planReq.getEndDtStr(),dtf),userId);

        Response response = planService.addPlan(plan);
        if(response.getResult().equals(ResultCode.OK)){
            return "plan/getMonthlyPlan";
        } else if(response.getResult().equals(ResultCode.INVALID_PARAM)){
            model.addAttribute("result",response.getResult());
            return "plan/addPlan";
        }else{
            model.addAttribute("result","다시 시도해주세요.");
            return "plan/addPlan";
        }
    }

    @RequestMapping("/modifyPage")
    public String modifyPage(Model model, @RequestParam("planId") String planId) {
        Plan plan = planService.getPlanById(planId);
        model.addAttribute("plan",plan);
        return "plan/modifyPlan";
    }

    @RequestMapping("/modifyPlan")
    public String modifyPlan(Model model, @RequestParam("planReq") PlanReq planReq){

        Plan plan = new Plan(planReq.getId(),planReq.getTitle(),planReq.getText(),LocalDate.parse(planReq.getStartDtStr(),dtf),LocalDate.parse(planReq.getEndDtStr(),dtf));

        Response response = planService.updatePlan(plan);
        if(response.getResult().equals(ResultCode.OK)){
            return "plan/getMonthlyPlan";
        } else if(response.getResult().equals(ResultCode.INVALID_PARAM)){
            model.addAttribute("result",response.getResult());
            return "plan/modifyPlan";
        }else{
            model.addAttribute("result","다시 시도해주세요.");
            return "plan/modifyPlan";
        }
    }

    @RequestMapping("/deletePlan")
    public String deletePlan(Model model, @RequestParam("planId")String planId){
        Response response = planService.deletePlan(planId);
        return "plan/getMonthlyPlan";
    }
}

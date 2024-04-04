package com.jRyun.demo.planProject.plan.controller;

import com.jRyun.demo.planProject.plan.domain.Plan;
import com.jRyun.demo.planProject.plan.domain.PlanReq;
import com.jRyun.demo.planProject.plan.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class PlanController {

    private PlanService planService;

    @Autowired
    public PlanController(PlanService planService){
        this.planService = planService;
    }

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
        model.addAttribute("plan",planService.getPlan(planId));
        return "plan/getPlan";
    }

    @RequestMapping("/addPage")
    public String addPage(Model model){
        return "plan/addPlan";
    }

    @RequestMapping("/addPlan")
    public String addPlan(Model model, @RequestParam){

    }
}

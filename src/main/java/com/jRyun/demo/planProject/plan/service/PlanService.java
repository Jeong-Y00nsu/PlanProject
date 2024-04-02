package com.jRyun.demo.planProject.plan.service;

import com.jRyun.demo.planProject.plan.domain.Plan;
import com.jRyun.demo.planProject.plan.mapper.PlanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PlanService {

    private PlanMapper planMapper;

    static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    @Autowired
    public PlanService(PlanMapper planMapper){
        this.planMapper = planMapper;
    }

}

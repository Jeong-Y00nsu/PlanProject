package com.jRyun.demo.planProject.plan.mapper;

import com.jRyun.demo.planProject.plan.domain.Plan;
import com.jRyun.demo.planProject.plan.domain.PlanReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PlanMapper {

    List<Plan> selectPlanByDt(@Param("planReq") PlanReq planReq);

    Plan selectById(@Param("id")String id);

    void insertPlan(@Param("plan")Plan plan);

    void deletePlanById(@Param("id")String id);

    void updatePlanById(@Param("plan") Plan plan);

    int countDuplicateId(@Param("id")String id);

}

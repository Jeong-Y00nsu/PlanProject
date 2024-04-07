package com.jRyun.demo.planProject.plan.service;

import com.jRyun.demo.planProject.plan.domain.Plan;
import com.jRyun.demo.planProject.plan.mapper.PlanMapper;
import com.jRyun.demo.planProject.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PlanService {

    private PlanMapper planMapper;

    static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    @Autowired
    public PlanService(PlanMapper planMapper){
        this.planMapper = planMapper;
    }

    public List<Map<LocalDate,List<Plan>>> getMonthlyPlan(String year, String month){
        LocalDate pivot = LocalDate.parse(year+month+"01", DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA));
        List<Map<LocalDate, List<Plan>>> result = new ArrayList<>();

        for(int i =1;i<=pivot.lengthOfMonth();i++){
            Map<LocalDate, List<Plan>> day = new HashMap<>();
            LocalDate tmpDate = pivot.withDayOfMonth(i);

            List<Plan> plans = planMapper.selectPlanByDt(tmpDate.toString());
            day.put(tmpDate,plans);

            result.add(day);
        }
        return result;
    }

    public List<Plan> getDailyPlan(String year, String month, String day){
        LocalDate current = LocalDate.parse(year+month+day, DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA));
        return planMapper.selectPlanByDt(current.toString());
    }

    public Plan getPlanById(String id){
        return planMapper.selectById(id);
    }

    public Response regPlan(Plan plan){
        try {
            Response result = validationRegParam(plan);
            if (result.getResult() != ResultCode.OK) return result;
            plan.setId(makePlanKey());
            planMapper.insertPlan(plan);
            return result;
        } catch (Exception e){
            logger.info("[PlanService][regPlan] fail makePlanKey");
            return new Response(ResultCode.FAIL,"일정 등록 실패");
        }
    }

    public Response updatePlan(Plan plan){
        Response result = validationUpdateParam(plan);
        if(result.getResult() != ResultCode.OK) return result;
        planMapper.updatePlanById(plan);
        return result;
    }

    public Response deletePlan(String planId){
        if(planMapper.countDuplicateId(planId)<1) return new Response(ResultCode.NOT_EXIST,"존재하지 않는 일정입니다.");
        planMapper.deletePlanById(planId);
        return new Response(ResultCode.OK,"일정을 삭제했습니다.");
    }

    private Response validationRegParam(Plan plan){
        //제목
        String[] chars = {"/","=","!","'"};
        if(plan.getTitle()==null ){
            return new Response(ResultCode.INVALID_PARAM,"제목을 입력해주세요.");
        }
        if(plan.getTitle().getBytes().length<=0 || plan.getTitle().getBytes().length>100){
            return new Response(ResultCode.INVALID_PARAM, "제목 byte길이는 0보다 크고 100 이하여야 합니다.");
        }
        if(Validation.isContainChars(plan.getTitle(),chars)){
            return new Response(ResultCode.INVALID_PARAM,"제목에 특수 문자가 포함됩니다.");
        }
        //내용
        if(plan.getText()==null){
            return new Response(ResultCode.INVALID_PARAM,"내용을 입력해주세요.");
        }
        if(plan.getText().getBytes().length<=0 || plan.getText().getBytes().length>30000){
            return new Response(ResultCode.INVALID_PARAM,"내용 byte길이는 0보다 크고 100 이하여야 합니다.");
        }
        if(Validation.isContainChars(plan.getText(),chars)){
            return new Response(ResultCode.INVALID_PARAM,"내용에 특수 문자가 포함됩니다.");
        }
        //날짜
        if(plan.getStartDt()==null || plan.getEndDt()==null){
            return new Response(ResultCode.INVALID_PARAM,"일정 시작 날짜와 끝나는 날짜를 입력해주세요.");
        }
        if(plan.getStartDt().isAfter(plan.getEndDt())){
            return new Response(ResultCode.INVALID_PARAM,"일정 시작 날짜는 끝나는 날짜보다 앞서야 합니다.");
        }

        return new Response(ResultCode.OK,"OK");
    }

    private Response validationUpdateParam(Plan plan){
        //제목
        String[] chars = {"/","=","!","'"};
        if(plan.getTitle()==null ){
            return new Response(ResultCode.INVALID_PARAM,"제목을 입력해주세요.");
        }
        if(plan.getTitle().getBytes().length<=0 || plan.getTitle().getBytes().length>100){
            return new Response(ResultCode.INVALID_PARAM, "제목 byte길이는 0보다 크고 100 이하여야 합니다.");
        }
        if(Validation.isContainChars(plan.getTitle(),chars)){
            return new Response(ResultCode.INVALID_PARAM,"제목에 특수 문자가 포함됩니다.");
        }
        //내용
        if(plan.getText()==null){
            return new Response(ResultCode.INVALID_PARAM,"내용을 입력해주세요.");
        }
        if(plan.getText().getBytes().length<=0 || plan.getText().getBytes().length>30000){
            return new Response(ResultCode.INVALID_PARAM,"내용 byte길이는 0보다 크고 100 이하여야 합니다.");
        }
        if(Validation.isContainChars(plan.getText(),chars)){
            return new Response(ResultCode.INVALID_PARAM,"내용에 특수 문자가 포함됩니다.");
        }
        //날짜
        if(plan.getStartDt()==null || plan.getEndDt()==null){
            return new Response(ResultCode.INVALID_PARAM,"일정 시작 날짜와 끝나는 날짜를 입력해주세요.");
        }
        if(plan.getStartDt().isAfter(plan.getEndDt())){
            return new Response(ResultCode.INVALID_PARAM,"일정 시작 날짜는 끝나는 날짜보다 앞서야 합니다.");
        }

        return new Response(ResultCode.OK,"OK");
    }

    public String makePlanKey() throws Exception{
        try {
            Cypher cypher = new Cypher();
            String planKey = "";

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String seed = MakeRandomStr.makeRandomPk(7);

            return cypher.encrypt(today + seed);
        } catch (NoSuchAlgorithmException e){
            logger.info("[PlanService][makePlanKey] 일정 Key 생성 중 예외 발생\n {}",e);
            throw new Exception(e);
        }
    }

}

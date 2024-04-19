package com.jRyun.demo.planProject.plan.service;

import com.jRyun.demo.planProject.plan.domain.Plan;
import com.jRyun.demo.planProject.plan.domain.PlanReq;
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

    /**
     * getMonthlyPlan
     * 입력으로 받은 월의 각 날짜가 포함되는 일정들을 조회
     *
     * @param year
     * @param month
     * @param userId
     * @return 월별 일정
     */
    public List<Map<LocalDate,List<Plan>>> getMonthlyPlan(String year, String month, String userId){
        logger.info("[PlanSerivce.getMonthlyPlan()] year={}, month={}, userId={}",year,month,userId);
        LocalDate pivot = LocalDate.parse(year+month+"01", DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA));
        List<Map<LocalDate, List<Plan>>> result = new ArrayList<>();

        for(int i =1;i<=pivot.lengthOfMonth();i++){
            Map<LocalDate, List<Plan>> day = new HashMap<>();
            LocalDate tmpDate = pivot.withDayOfMonth(i);

            List<Plan> plans = planMapper.selectPlanByDt(makeDateRange(tmpDate,userId));
            day.put(tmpDate,plans);

            result.add(day);
        }
        logger.info("[PlanService.getMonthlyPlan()] response");
        return result;
    }

    /**
     * getDailyPlan
     *  입력으로 받은 날짜가 포함되는 일정들을 조회
     *  
     * @param year
     * @param month
     * @param day
     * @param userId
     * @return 일별 일정
     */
    public List<Plan> getDailyPlan(String year, String month, String day, String userId){
        logger.info("[PlanService.getDailyPlan()] year={}, month={}, day={}, userId={}",year,month,day,userId);
        LocalDate current = LocalDate.parse(year+month+day, DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA));
        logger.info("[PlanService.getDailyPlan()] response");
        return planMapper.selectPlanByDt(makeDateRange(current, userId));
    }

    /**
     * getPlanById
     * 입력으로 받은 일정 ID를 가지고 일정 조회
     *
     * @param planId
     * @return
     */
    public Plan getPlanById(String planId){
        logger.info("[PlanService.getPlanById()] id={}",planId);
        return planMapper.selectById(planId);
    }

    /**
     * addPlan
     * 일정 추가
     *
     * @param plan
     * @return
     */
    public Response addPlan(Plan plan){
        logger.info("[PlanService.addPlan()] plan={}",plan.toString());
        try {
            Response result = validationRegParam(plan);
            if (result.getResult() != ResultCode.OK) return result;
            plan.setId(makePlanKey());
            planMapper.insertPlan(plan);
            logger.info("[PlanService.addPlan()] Add plan success");
            return result;
        } catch (Exception e){
            logger.info("[PlanService.regPlan()] Error");
            return new Response(ResultCode.FAIL,"일정 등록 실패");
        }
    }

    /**
     * updatePlan
     * 일정 수정
     *
     * @param plan
     * @return
     */
    public Response updatePlan(Plan plan){
        logger.info("[PlanService.updatePlan()] plan={}",plan.toString());
        try{
            Response result = validationUpdateParam(plan);
            if(result.getResult() != ResultCode.OK) return result;
            planMapper.updatePlanById(plan);
            logger.info("[PlanService.updatePlan()] Modify plan success");
            return result;
        } catch(Exception e){
            logger.info("[PlanService.updatePlan()] Error");
            return new Response(ResultCode.FAIL,"일정 수정 실패");
        }

    }

    /**
     * deletePlan
     * 일정 삭제
     *
     * @param planId
     * @return
     */
    public Response deletePlan(String planId){
        logger.info("[PlanService.deletePlan()] planId={}",planId);
        if(planMapper.countDuplicateId(planId)<1) {
            logger.info("[PlanService.deletePlan()] Not exist plan");
            return new Response(ResultCode.NOT_EXIST,"존재하지 않는 일정입니다.");
        }
        planMapper.deletePlanById(planId);
        logger.info("[PlanService.deletePlan()] Delete plan success");
        return new Response(ResultCode.OK,"일정을 삭제했습니다.");
    }

    /**
     * validationRegParam
     * 일정 추가를 위해 입력으로 들어온 일정 검증
     *
     * @param plan
     * @return
     */
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

    /**
     * validationUpdateParam
     * 일정 수정을 위해 입력으로 들어온 일정 검증
     * 
     * @param plan
     * @return
     */
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

    /**
     * makePlanKey
     * 일정 추가 시 필요한 일정 ID 생성 메소드
     *
     * @return
     * @throws Exception
     */
    public String makePlanKey() throws Exception{
        try {
            Cypher cypher = new Cypher();

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String seed = MakeRandomStr.makeRandomPk(7);

            return cypher.encrypt(today + seed);
        } catch (NoSuchAlgorithmException e){
            logger.info("[PlanService.makePlanKey()] 일정 Key 생성 중 예외 발생: {}",e.getMessage());
            throw new Exception(e);
        }
    }

    /**
     * makeDateRange
     * 입력으로 기준 날짜가 들어오면 조회를 위한 날짜 범위를 만들어주는 메소드
     * 시작일자는 기준 날짜의 0시, 종료일자는 기준 날짜 다음날 0시
     *
     * @param pivotDate
     * @param userId
     * @return PlanReq.startDt, PlanReq.endDt
     */
    private PlanReq makeDateRange(LocalDate pivotDate, String userId){
        PlanReq result = new PlanReq();
        result.setUserId(userId);
        result.setStartDt(pivotDate.atStartOfDay());
        result.setEndDt(pivotDate.plusDays(1).atStartOfDay());
        return result;
    }

}

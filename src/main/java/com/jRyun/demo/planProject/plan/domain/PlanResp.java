package com.jRyun.demo.planProject.plan.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class PlanResp {

    private int year; //현재 년도
    private int month; // 현재 달
    private int cntOfWeeks; // 현재 달의 주 수
    private int firstDayOfWeek; // 현재 달 첫번째 날의 요일 (0 base)
    private int lastDay; //마지막 일수
    List<Map<LocalDate, List<Plan>>> plans; // 현재 달 일별 일정 리스트

}

package com.jRyun.demo.planProject.plan.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PlanReq {

    private int year;
    private int month;
    private int day;

    private int startYear;
    private int startMonth;
    private int startDay;
    private String startDtStr;

    private LocalDateTime startDt;

    private int endYear;
    private int endMonth;
    private int endDay;
    private String endDtStr;

    private LocalDateTime endDt;

    private String title;
    private String text;
    private String id;

    private String userId;

}

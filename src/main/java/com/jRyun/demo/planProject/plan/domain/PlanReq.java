package com.jRyun.demo.planProject.plan.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanReq {

    private int year;
    private int month;
    private int day;

    private int startYear;
    private int startMonth;
    private int startDay;
    private String startDtStr;

    private LocalDate startDt;

    private int endYear;
    private int endMonth;
    private int endDay;
    private String endDtStr;

    private LocalDate endDt;

    private String title;
    private String text;
    private String id;

}

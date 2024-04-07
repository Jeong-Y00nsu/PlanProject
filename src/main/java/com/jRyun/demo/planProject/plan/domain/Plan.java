package com.jRyun.demo.planProject.plan.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter @Getter
public class Plan implements Serializable, Comparable<Plan> {

    private String id;
    private String title;
    private String text;
    private LocalDate startDt;
    private LocalDate endDt;

    private String planId;

    public Plan(){}
    public Plan(String id, String title, String text, LocalDate startDt, LocalDate endDt) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.startDt = startDt;
        this.endDt = endDt;
    }

    public Plan(String title, String text, LocalDate startDt, LocalDate endDt) {
        this.title = title;
        this.text = text;
        this.startDt = startDt;
        this.endDt = endDt;
    }

    @Override
    public String toString() {
        return "["+this.getId()+"] title: "+this.getTitle()+", text: "+this.getText()+", start: "+this.getStartDt()+", end: "+this.getEndDt();
    }

    @Override
    public int compareTo(Plan o) {
        if(this.startDt.compareTo(o.startDt)!=0) {
            return this.startDt.compareTo(o.startDt);
        } else {
            return this.endDt.compareTo(o.endDt);
        }
    }
}

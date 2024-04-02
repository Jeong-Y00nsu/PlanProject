package com.jRyun.demo.planProject.util;

import lombok.Data;

@Data
public class Response {

    ResultCode result;
    String message;
    Object returnValue;

    public Response(){}

    public Response(ResultCode result, String message){
        this.result = result;
        this.message = message;
    }
}

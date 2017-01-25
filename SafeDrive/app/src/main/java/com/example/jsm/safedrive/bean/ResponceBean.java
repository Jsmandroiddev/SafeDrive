package com.example.jsm.safedrive.bean;

/**
 * Created by Amit Gupta on 05-05-2016.
 */
public class ResponceBean {
    String status;
    Object response;
    String message;
    String errorCode;

    public ResponceBean(String status, Object response, String message, String errorCode) {
        this.status = status;
        this.response = response;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

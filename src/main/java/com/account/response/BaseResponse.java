package com.account.response;

public class BaseResponse {

    private int status;
    private boolean success;
    private String message;

    public BaseResponse(int status, boolean success) {
        this.status = status;
        this.success = success;
    }

    public BaseResponse(int status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

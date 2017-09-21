package com.account.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public class PatchRequest {
    private String op;
    private String path;
    private Object value;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

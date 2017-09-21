package com.account.response;

import com.account.model.Account;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ObjectResponse<T> extends BaseResponse {
   private List<T> objectList = new ArrayList();

    private T object;

    public ObjectResponse(int status, boolean success, List<T> objectList) {
        super(status, success);
        this.objectList = objectList;
    }
    public ObjectResponse(int status, boolean success, T object) {
        super(status, success);
        this.object = object;
    }

    public ObjectResponse(int status, boolean success, String message) {
        super(status, success, message);
    }

    public ObjectResponse(int status, boolean success) {
        super(status, success);
    }

    public List<T> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<T> objectList) {
        this.objectList = objectList;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}

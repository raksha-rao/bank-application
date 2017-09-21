package com.account.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public class AccountRequest {
    private String name;
    private String currency;
    private Double depositAmount;

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

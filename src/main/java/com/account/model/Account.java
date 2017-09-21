package com.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jersey.repackaged.com.google.common.base.Objects;

@JsonDeserialize(builder = Account.Builder.class)
public class Account {

    private Long accountNumber;
    // Name can be represented as a nested Json property with first, middle and last names as fields.
    private String name;
    private String currency;
    private Double balance = 0.00;

    private Account(Builder builder) {
        accountNumber = builder.number;
        name = builder.name;
        currency = builder.currency;
        balance = builder.balance;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("number")
    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }


    public static final class Builder {
        private Long number;
        private String name;
        private String currency;
        private Double balance;

        public Builder() {
        }

        public Builder withNumber(Long val) {
            number = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }


        public Builder withCurrency(String val) {
            currency = val;
            return this;
        }

        public Builder withBalance(Double val) {
            balance = val;
            return this;
        }

        public Account build() {
            return new Account(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!accountNumber.equals(account.accountNumber)) return false;
        if (!name.equals(account.name)) return false;
        if (!currency.equals(account.currency)) return false;
        return balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accountNumber.hashCode(),name.hashCode(),currency.hashCode(),balance.hashCode());
        return result;
    }
}

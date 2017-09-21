package com.account.response;

import com.account.model.Account;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountResponse extends BaseResponse {
   private List<Account> accounts = new ArrayList<Account>();

    private Account account;

    public AccountResponse(int status, boolean success, List<Account> accounts) {
        super(status, success);
        this.accounts = accounts;
    }
    public AccountResponse(int status, boolean success, Account account) {
        super(status, success);
        this.account = account;
    }

    public AccountResponse(int status, boolean success, String message) {
        super(status, success, message);
    }

    public AccountResponse(int status, boolean success) {
        super(status, success);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

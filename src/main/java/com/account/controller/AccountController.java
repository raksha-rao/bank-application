package com.account.controller;

import com.account.model.Account;
import com.account.request.AccountRequest;
import com.account.request.PatchRequest;
import com.account.response.AccountResponse;
import com.account.response.BaseResponse;
import com.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Component
@Path("/v1/accounts/account")
@Produces("application/json")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Value("${json.account_details}")
    private String accountDetailsFile;

    @GET
    public BaseResponse getAllAccounts() {
        List<Account> accountList;
        BaseResponse response;
        try {
            accountList = accountService.getAccountsFromFile(accountDetailsFile);
            response = new AccountResponse(200,true, accountList);
        } catch (IOException e) {
            response = new AccountResponse(500,false, e.getMessage());
        }
        return response;
    }


    @GET
    @Path("/{accountId}")
    public BaseResponse getAccount(@PathParam("accountId") String accountId) {
        BaseResponse response;
        try {
            Account account = accountService.findAccount(accountId, accountDetailsFile);
            response = new AccountResponse(200,true, account);
        } catch (IOException e) {
            response = new AccountResponse(500,false, e.getMessage());
        }

        return response;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse createAccount(AccountRequest accountRequest) {
        BaseResponse response;
        try {
            Long accountNumber = accountService.createAccount(accountRequest, accountDetailsFile);
            response = new AccountResponse(201,true);
            String message = "Account created with account number "+ accountNumber;
            response.setMessage(message);
        } catch (IOException e) {
            response = new AccountResponse(500,false, e.getMessage());
        }
        return response;
    }


    @PATCH
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse patchAccount(List<PatchRequest> patchRequestList, @PathParam("accountId") String accountId) {
        BaseResponse response;
        try {
            accountService.updateAccount(patchRequestList, accountId, accountDetailsFile);
            response = new AccountResponse(200,true);
        } catch (Exception e) {
            response = new AccountResponse(500,false, e.getMessage());
        }

        return response;
    }
}

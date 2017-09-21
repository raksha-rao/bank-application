package com.account.controller;

import com.account.model.Account;
import com.account.request.AccountRequest;
import com.account.request.PatchRequest;
import com.account.response.ObjectResponse;
import com.account.response.BaseResponse;
import com.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Path("/v1/accounts/account")
@Produces("application/json")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Value("${json.account_details}")
    private String accountDetailsFile;

    Logger logger = LoggerFactory.getLogger("AccountController");

    @GET
    public BaseResponse getAllAccounts() {
        List<Account> accountList;
        BaseResponse response;
        try {
            accountList = accountService.getAccountsFromFile(accountDetailsFile);
            // Avoid duplicate accounts
            Set accountSet = accountList.stream().collect(Collectors.toCollection(LinkedHashSet::new));
            response = new ObjectResponse(HttpServletResponse.SC_OK,true, accountSet);
            if( accountList == null || accountList.isEmpty() ){
                response.setMessage("No accounts found!");
            }
        } catch (Exception e) {
            response = new ObjectResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,false, e.getMessage());
        }
        return response;
    }


    @GET
    @Path("/{accountId}")
    public BaseResponse getAccount(@PathParam("accountId") String accountId) {
        BaseResponse response;
        try {
            Account account = accountService.findAccount(accountId, accountDetailsFile);
            response = new ObjectResponse(HttpServletResponse.SC_OK,true, account);
            if(account == null){
                response.setMessage("Account not found!");
            }
        } catch (Exception e) {
            response = new ObjectResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,false, e.getMessage());
        }

        return response;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse createAccount(AccountRequest accountRequest) {
        BaseResponse response;
        try {
            Long accountNumber = accountService.createAccount(accountRequest, accountDetailsFile);
            response = new ObjectResponse(HttpServletResponse.SC_CREATED,true);
            String message = "Account created with account number "+ accountNumber;
            response.setMessage(message);
        } catch (Exception e) {
            response = new ObjectResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,false, e.getMessage());
        }
        return response;
    }


    @PATCH
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse patchAccount(List<PatchRequest> patchRequestList) {
        BaseResponse response;
        try {
            accountService.updateAccount(patchRequestList, accountDetailsFile);
            response = new ObjectResponse(HttpServletResponse.SC_OK,true);
            response.setMessage("Account updated successfully!");
        } catch (Exception e) {
            response = new ObjectResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,false, e.getMessage());
        }

        return response;
    }
}

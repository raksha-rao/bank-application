package com.account.controller;

import com.account.controller.AccountController;
import com.account.model.Account;
import com.account.request.AccountRequest;
import com.account.response.BaseResponse;
import com.account.service.AccountService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Test class for Account Controller Test
public class AccountControllerTest {

    @InjectMocks
    AccountController accountController = new AccountController();

    @Mock
    private AccountService accountService;

    @BeforeMethod(alwaysRun=true)
    public void start(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters("accountDetailsFile")
    public void createAccountTest(String accountDetailsFile) throws IOException {
        AccountRequest accountRequest = new AccountRequest();
        Mockito.when(accountService.createAccount(accountRequest,accountDetailsFile)).thenReturn(1L);

        BaseResponse response = accountController.createAccount(accountRequest);
        Assert.assertNull(response.isSuccess());
    }

    @Test
    @Parameters("accountDetailsFile")
    public void getAllAccountsTest(String accountDetailsFile) throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        List<Account> accountList = new ArrayList<>();
        Mockito.when(accountService.getAccountsFromFile(accountDetailsFile)).thenReturn(accountList);

        BaseResponse response = accountController.getAllAccounts();
        Assert.assertNotNull(response.isSuccess());
        System.out.println(response.getMessage()+response.getStatus()+response.isSuccess());
    }

    @Test
    @Parameters("currency" )
    public void getAccountTest(String currency) throws Exception {
        String accountDetailsFile = null;
        AccountRequest accountRequest = new AccountRequest();
        Account account = new Account.Builder().withNumber(1L).withCurrency(currency).withName("Hello World").build();
        Mockito.when(accountService.findAccount("1", accountDetailsFile)).thenReturn(account);

        BaseResponse response = accountController.getAccount("1");
        Assert.assertNotNull(response.isSuccess());
        System.out.println(response.getMessage()+response.getStatus()+response.isSuccess());
    }
}

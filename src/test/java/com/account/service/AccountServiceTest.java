package com.account.service;

import com.account.request.AccountRequest;
import com.account.request.PatchRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;


    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters("accountDetailsFile")
    public void testGetAccountsFromFile(String accountDetailsFile) throws Exception {
        accountService.getAccountsFromFile(accountDetailsFile);
    }

    @Test
    @Parameters("accountDetailsFile")
    public void testFindAccount(String accountDetailsFile) throws Exception {
        accountService.findAccount("1",accountDetailsFile);
    }

    @Test
    @Parameters("accountDetailsFile")
    public void testCreateAccount(String accountDetailsFile) throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountService.createAccount(accountRequest,accountDetailsFile);
    }

    @Test
    @Parameters("accountDetailsFile")
    public void testUpdateAccount(String accountDetailsFile) throws Exception {
        List<PatchRequest> patchRequestList = new ArrayList<PatchRequest>();
        accountService.updateAccount(patchRequestList,accountDetailsFile);
    }

}
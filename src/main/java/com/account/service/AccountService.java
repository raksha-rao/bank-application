package com.account.service;

import com.account.model.Account;
import com.account.request.AccountRequest;
import com.account.request.PatchRequest;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final Long INITIAL_VALUE = 100000L;
    private final String ACCOUNTS_ROOT_NODE = "accounts";
    private ObjectMapper mapper = new ObjectMapper();


    public List<Account> getAccountsFromFile(String fileName) throws IOException {
        List<Account> accountList = new ArrayList<Account>();
        Iterator<JsonNode> iterator = getJsonNodeIterator(fileName, mapper);
        while (iterator.hasNext()) {
            Account account = mapper.readValue(iterator.next().traverse(), Account.class);
            accountList.add(account);
        }
        return accountList;
    }

    public Iterator<JsonNode> getJsonNodeIterator(String fileName, ObjectMapper mapper) throws IOException {
        File file = new File(fileName);
        JsonNode root = mapper.readTree(file);
        ArrayNode accounts = (ArrayNode) root.path(ACCOUNTS_ROOT_NODE);

        return accounts.elements();
    }

    // search account based on account number in the json file
    public Account findAccount(String accountId, String fileName) throws IOException {
        File file = new File(fileName);
        JsonNode root = mapper.readTree(file);
        ArrayNode accounts = (ArrayNode) root.path("accounts");
        for (JsonNode node : accounts) {
            Account account = mapper.readValue(node.traverse(), Account.class);
            if ((Long.parseLong(accountId)) == (account.getAccountNumber())) {
                return account;
            }
        }
        return null;
    }

    //open new account
    public Long createAccount(AccountRequest accountRequest, String fileName) throws IOException {
        Random random = new Random(System.currentTimeMillis());
        Long accountNumber = INITIAL_VALUE + random.nextInt(900000);
        // There is a  possibility that a duplicate account number could be created.
        Account account = new Account.Builder().withNumber(accountNumber).withName(accountRequest.getName())
                .withCurrency(accountRequest.getCurrency())
                .withBalance(accountRequest.getDepositAmount()).build();

        writeToFile(account, fileName);
        return account.getAccountNumber();
    }

    //update json file with additional account
    private void writeToFile(Account account, String fileName) throws IOException {

        File file = new File(fileName);

        JsonNode rootNode = mapper.readTree(file);
        ArrayNode accounts = (ArrayNode) rootNode.path("accounts");

        //accounts.add(accountNode);

        //JsonNode root = mapper.createObjectNode();
        //ArrayNode arrayNode = ((ObjectNode) rootNode).putArray(ACCOUNTS_ROOT_NODE);
        JsonNode accountNode = mapper.convertValue(account, JsonNode.class);
        accounts.add(accountNode);

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);
        mapper.writeTree(jsonGenerator, rootNode);
        jsonGenerator.close();

    }

    //update details on account
    @Transactional
    public void updateAccount(List<PatchRequest> patchRequestList, String accountId, String fileName) throws Exception {
        try {
            File file = new File(fileName);
            JsonNode rootNode = mapper.readTree(file);
            for (PatchRequest patchRequest : patchRequestList) {
                String[] pathStrings = patchRequest.getPath().split("/");
                int index = Integer.parseInt(pathStrings[2]);
                ArrayNode arrayNode = (ArrayNode) rootNode.path(pathStrings[1]);

                JsonNode toBeUpdatedNode = arrayNode.get(index);
                ((ObjectNode) toBeUpdatedNode).put(pathStrings[3], patchRequest.getValue());


                arrayNode.set(index, toBeUpdatedNode);
            }
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(file, JsonEncoding.UTF8);
            mapper.writeTree(jsonGenerator, rootNode);
        }
        catch (Exception e){
            //log error
            throw e;
        }
    }

}

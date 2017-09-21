package com.account.service;

import com.account.constants.PatchActions;
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
import java.util.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final Long INITIAL_VALUE = 100000L;
    private final int bound = 900000;
    private final String ACCOUNTS_ROOT_NODE = "accounts";
    private final ObjectMapper mapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger("AccountService");

    public List<Account> getAccountsFromFile(String fileName) throws Exception {
        List<Account> accountList = new ArrayList<>();
        ArrayNode nodes = getNodes(fileName, mapper);
        for (JsonNode node : nodes) {
            Account account = mapper.readValue(node.traverse(), Account.class);
            accountList.add(account);
        }
        return accountList;
    }

    private ArrayNode getNodes(String fileName, ObjectMapper mapper) throws IOException {
        File file = new File(fileName);
        JsonNode root = mapper.readTree(file);
        return (ArrayNode) root.path(ACCOUNTS_ROOT_NODE);
    }

    /**
     * search account based on account number in the json file
     **/
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

    /**
     * open new account
     **/
    public Long createAccount(AccountRequest accountRequest, String fileName) throws IOException {
        Random random = new Random(System.currentTimeMillis());
        Long accountNumber = INITIAL_VALUE + random.nextInt(bound);
        // There is a  possibility that a duplicate account number could be created.
        Account account = new Account.Builder().withNumber(accountNumber).withName(accountRequest.getName())
                .withCurrency(accountRequest.getCurrency())
                .withBalance(accountRequest.getDepositAmount()).build();

        //Throws NPE is any variables of account is null
        Optional<Account> op = Optional.of(account);
        if (op.isPresent()) {
            writeToFile(account, fileName);
        }
        return account.getAccountNumber();
    }

    /**
     * update json file with additional account
     **/
    private void writeToFile(Account account, String fileName) throws IOException {

        File file = new File(fileName);

        JsonNode rootNode = mapper.readTree(file);
        ArrayNode accounts = (ArrayNode) rootNode.path("accounts");

        JsonNode accountNode = mapper.convertValue(account, JsonNode.class);
        accounts.add(accountNode);

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);
        mapper.writeTree(jsonGenerator, rootNode);
        jsonGenerator.close();

    }

    /**
     * update details on account
     **/
    @Transactional
    public void updateAccount(List<PatchRequest> patchRequestList, String fileName) throws Exception {
        // Account id is not used to fetch the accounts. We use request directions to find and update the json.
        try {
            File file = new File(fileName);
            JsonNode rootNode = mapper.readTree(file);

            for (PatchRequest patchRequest : patchRequestList) {
                if (patchRequest.getOp().equalsIgnoreCase(PatchActions.REPLACE.toString())) {
                    String[] pathStrings = patchRequest.getPath().split("/");
                    int index = Integer.parseInt(pathStrings[2]);
                    ArrayNode arrayNode = (ArrayNode) rootNode.path(pathStrings[1]);

                    JsonNode toBeUpdatedNode = arrayNode.get(index);
                    Object value = patchRequest.getValue();

                    if (value instanceof String) {
                        ((ObjectNode) toBeUpdatedNode).put(pathStrings[3], (String) value);
                    } else if (value instanceof Long) {
                        ((ObjectNode) toBeUpdatedNode).put(pathStrings[3], (Long) value);
                    } else if (value instanceof Double) {
                        ((ObjectNode) toBeUpdatedNode).put(pathStrings[3], (Double) value);
                    }

                    arrayNode.set(index, toBeUpdatedNode);
                }
            }
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(file, JsonEncoding.UTF8);
            mapper.writeTree(jsonGenerator, rootNode);
        } catch (Exception e) {
            logger.error("Exception thrown while updating account for request: ", patchRequestList);
            throw e;
        }
    }

}

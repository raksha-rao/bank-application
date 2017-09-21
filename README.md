# bank-application
A simple Bank Application to add, update, search accounts using Spring Boot.

Prerequisites:
Java 1.8

Usage
Run the following commands on the terminal
mvn clean package to package the jar file in /target directory
mvn spring-boot:run to run the application

You can use the following API's in Postman

REST APIs
/v1/accounts/account - Create an account, JSON input(POST)

Sample Input 
{"name": "Mary Jane","currency": "GBP","depositAmount": 1000}
Sample Output
{
    "status": 201,
    "success": true,
    "message": "Account created with account number 547321"
}

/v1/accounts/account/account_id - Get details of an account(GET)

Sample Output
{
    "status": 200,
    "success": true,
    "object": {
        "name": "John Nash",
        "currency": "GBP",
        "balance": 100,
        "number": 237453
    }
}

/v1/accounts/account/account_id - Update an account(PATCH)

Sample Input
[{ "op": "replace", "path": "/accounts/1/name", "value": "Steven Wilson" },
{ "op": "replace", "path": "/accounts/0/balance", "value": 10200.0 }
]

Sample Output
{
    "status": 200,
    "success": true,
    "message": "Account updated successfully!"
}


/v1/accounts/account - list all the accounts(GET)

Sample Output
{
    "status": 200,
    "success": true,
    "object": [
        {
            "name": "Mook Kim Juang",
            "currency": "USD",
            "balance": 10200,
            "number": 100001
        },
        {
            "name": "Steven Wilson",
            "currency": "EUR",
            "balance": 200,
            "number": 100002
        }
    ]
}
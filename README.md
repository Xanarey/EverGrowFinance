# EverGrowFinance Overview

EverGrowFinance is a dynamic platform designed to streamline user transactions, offering features such as video service subscriptions, user authentication, card linkage to wallets, transaction verification, and automated fund deductions based on subscription terms.

## Technologies

- **Java 17**: Core programming language.
- **Spring Security + JWT**: For robust authentication and authorization.
- **PostgreSQL**: Database management.
- **Redis**: Employed as a caching solution.
- **Spring JPA**: For database interaction.
- **Automated Tests**: Covering key layers of the application.



## Endpoints

EverGrowFinance exposes several RESTful endpoints for user interaction:

### Authentication
Authenticate users and obtain a JWT token.

POST http://localhost:8080/auth

Body:
{
"email": "admin@gmail.com",
"password": "12345"
}


### User Details
Retrieve details of a specific user.


GET http://localhost:8080/users/{userId}


### Subscription Management
Create a new subscription or delete an existing one.


Create a new subscription


POST http://localhost:8080/subscriptions/create

Body:
{
// Subscription details here
}

Delete a subscription

DELETE http://localhost:8080/subscriptions/{subscriptionId}


### Balance Inquiry
Check the balance of a user's account.

GET http://localhost:8080/balance

Body:
{
"phoneNumber": 89033543683
}



### Fund Transfer
Transfer funds between user accounts.

POST http://localhost:8080/transfer

Body:
{
"fromPhoneNumber": 89997675428,
"toPhoneNumber": 89153608090,
"amount": 2500
}


### Transaction History
View all transactions for a user.

GET http://localhost:8080/transactions/all
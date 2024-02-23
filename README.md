EverGrowFinance
Overview
EverGrowFinance is a dynamic platform designed to streamline user transactions, offering features such as video service subscriptions, user authentication, card linkage to wallets, transaction verification, and automated fund deductions based on subscription terms.

Technologies
Java 17: Core programming language
Spring Security + JWT: For robust authentication and authorization
PostgreSQL: Database management
Redis: Employed as a caching solution
Spring JPA: For database interaction
Automated Tests: Covering key layers of the application
Endpoints
EverGrowFinance exposes several RESTful endpoints for user interaction:

Authentication

bash
Copy code
POST http://localhost:8080/auth
Body: {"email":"admin@gmail.com", "password":"12345"}
Response includes a JWT token for subsequent requests.

User Details

bash
Copy code
GET http://localhost:8080/users/{userId}
Subscription Management

bash
Copy code
POST http://localhost:8080/subscriptions/create
DELETE http://localhost:8080/subscriptions/{subscriptionId}
Balance Inquiry

bash
Copy code
GET http://localhost:8080/balance
Fund Transfer

bash
Copy code
POST http://localhost:8080/transfer
Body: {"fromPhoneNumber": 89997675428, "toPhoneNumber": 89153608090, "amount": 2500}
Transaction History

bash
Copy code
GET http://localhost:8080/transactions/all
Contact
For any queries or contributions, please reach out via Telegram: @engend

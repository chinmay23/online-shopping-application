# online-shopping-application
This repository has the source code for the backend of a online shopping application
The project is divided into three microservices
1.	Product Service: Has POST endpoint to add products to the application and a GET endpoint to retrieve products
2.	Order Service: This service has a POST endpoint to place an order. The order service is in synchronous communication with the inventory service to validate availability of products
3.	Inventory Service: This service has a GET endpoint which is returns a boolean whether a product with given quantity is in the inventory
4.	Notification Service: This service is used to notify the user via email that the order is successfully placed. This service is in async communcation with the order service

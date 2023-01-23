# Kezbek Service

Kezbek service is Backend Application built using Spring Boot as the main framework and use microservices architecture. The purpose of this application is to allow partner to process cashback with sophisticated rules & customer will receive cashback from purchasing items from partner online shop.

## Main Feature

- Processing cashback from partner transaction
- Register partner
- Count potential cashback and send cashback to customer
- Send Email Notification to Customer

## Architecure
![Text](/public/architecture.png)

## **Tech Stack**

- Spring Boot 
- Amazon SQS for messaging queue
- Amazon SES for sending email
- Drools for rule engine processing
- Swagger for API documentation
- PostgreSQL with spring data JPA

## **Tech Database**
Actualy the database is not normally well, the design is flat except the rule related data, so in the future the design can fit using any database technology no exception with nosql db, thanks for sping data jpa with the magic repository, we can easily move to another database tech

![Text](/public/erd.jpg)


## **How to Run**

To run the application, please clone this repo and run mvn clean install
Alternatively can download docker-compose completely to run docker image from gitlab docker-hub



## **API Documentation**

API documentation is available through Swagger at **`[http://localhost:9000/swagger-ui.html]`** (http://localhost:9000/swagger-ui.html.at) each service

For Agregate documentation you can check at the url :

- [Postman Api Docs](https://api.postman.com/collections/9962266-f53ca735-0b94-4bee-b3cd-40e90ca1422e?access_key=PMAT-01GQFKCPDD68SG0PKWGHRBEXEF)

## **Email Notifications**
The application utilizes the Amazon SES to send email notifications to customers when they have earned cashback.


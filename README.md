# DOTPAY-TASK
Take-home challenge for java backend role at DotPay Africa

### IMPLEMENTATION KEY POINTS
- Singleton was employed as the design pattern (ensuring no multiple instances of a class is being created )
- JUnit and Mokito was used for the automated testing
- Java Persistence API was used as persistence layer
- Springboot scheduler was used for scheduling jobs
- Every request return a uniform object which will make the user have a similar experience using the exposed APIs


### RUNNING THE APP
- Clone this project, update the project (POM file)
- Start the main class from your IDE
- Import the postman collection sent via the email to your postman workspace and test the apis in there
Alternatively, you can create docker image, push to container registry and deploy to kubernetes cluster.
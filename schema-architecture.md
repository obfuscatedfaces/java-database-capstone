Section 1:

This Spring Boot application uses both MVC and REST controllers. Thymeleaf 
templates are used for the Admin and Doctor dashboards, while REST APIs 
serve all other modules. The application interacts with two 
databases—MySQL (for patient, doctor, appointment, and admin data) and 
MongoDB (for prescriptions). All controllers route requests through a 
common service layer, which in turn delegates to the appropriate 
repositories. MySQL uses JPA entities while MongoDB uses document models.

Section 2:

1. User accesses AdminDashboard or Appointment pages
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer
4. The service layer handles all business logic
5. The service layer routes the requests to the needed database repository
6. The database repositories access the needed DB
7. The needed DB access the models which return associated data 

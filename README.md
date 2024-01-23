<b>Comparus test task</b>

Service for aggregating data from multiple databases. Provides a single rest endpoint for selecting user data,
selected from all databases according to application.yaml config file.

The service uses Spring BeanDefinitionRegistryPostProcessor to dynamically register dedicated amount of 
EntityManagerFactory beans with related datasources based on .yaml config file.
Obtaining data from databases is implemented by using jpa EntityManager within related persistence unit.

Database: H2

JpaVendor: Hibernate

Build tool: Maven
___

REST endpoint to get users: GET /v1/users?name=(optional)

OpenAPI link http://localhost:8080/swagger-ui.html

To get application launched OpenApi classes should be generated. Use --mvn clean install




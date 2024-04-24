<h1>OleksandrOrlovIt/UserRest</h1>
<h2>Summary</h2>
<p>This is a simple REST application to manage Users. </p>
<h3>Tech Stack</h3>
<p>Spring Boot Starter Parent (v3.2.5): Spring Boot Starter Web, Spring Boot Starter Test, Spring Boot Starter HATEOAS, Hibernate Validator, Lombok, Jackson Datatype JSR310, SpringDoc OpenAPI Starter WebMVC UI (v2.5.0), Spring Boot Maven Plugin (v3.2.4),
Maven Compiler Plugin (v3.8.1), Maven Surefire Plugin (v3.0.0-M5), JaCoCo Maven Plugin (v0.8.11)</p>
<h2>Work that has been done</h2>
<h3>Model layer</h3>
<p>User class has the following fields:
1.1. Email (required). Add validation against email pattern
1.2. First name (required)
1.3. Last name (required)
1.4. Birth date (required). Value must be earlier than current date
1.5. Address (optional)
1.6. Phone number (optional)
</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/abad4fe6-42f8-4bfb-8d52-6a8a0f2e904f)

<h3>Service layer</h3>
<p>There was created an interface with all important methods and an implementation with errorHandling for edge cases</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/dc175636-71c5-4370-bc40-30a36c31327e)

<p>In order to retrieve a value from properties, PropertyConfig was created</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/53ea9a8e-b1d6-4e33-a389-f8dfb81848cb)

<h3>Controller layer</h3>
<p>Controller layer has validation for input field and all of the requested http methods</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/e6c8ee8b-0864-4e26-8863-e334c9ec6f3a)


<h3>Functionality</h3>
<p>Application the following functionality:
2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
2.2. Update one/some user fields
2.3. Update all user fields
2.4. Delete user
2.5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects
</p>
<p>After running application and using "http://localhost:8080/swagger-ui/index.html#/" url you can see and use all of the application methods </p>

  ![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/d2a4312e-d765-4a4f-9f34-6c3cb98f4cb9)

<h3>Code Coverage</h3>
<p>3. Code is covered by unit tests using Spring and Jacoco for code coverage results</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/fac8a7af-f4c2-4552-897a-d9e913d38ec1)

<h3>Error handling</h3>
<p>Code has error handling for REST</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/da182faf-750c-44bf-a171-c02cdd6b3d4a)

<h3>Reponses</h3>
<p>API responses are in JSON format</p>

![image](https://github.com/OleksandrOrlovIT/UserREST/assets/86959421/00323c35-6928-4673-808a-3d896d2941d5)


## Run
Run the main class [`ProblemWebDemoApplication`](src/main/java/com/ksoot/web/auth/SpringBootWebAuthApplication.java)
and access Swagger [`Swagger`](http://localhost:8080/swagger-ui.html) at http://localhost:8080/swagger-ui.html

Select `Application` from dropdown **Select a definition**

* **Actuator** APIs, configured as unsecured in [`SecurityConfiguration`](src/main/java/com/ksoot/web/auth/security/SecurityConfiguration.java).
* **Api** Demo secured APIs.

**Click on Authorize button** to pass the JWT Token. Use any valid JWT Token.
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```



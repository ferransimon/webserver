#Java 8 web server

This project is a web server made with java 8. It comes with a basic url routing implementation,
login and session management, Html rendering and content negotiation based on http headers.

#Available pages

* /login
* /home
* /page1
* /page2
* /page3

#REST API Resources

* GET /api/users - list all users
* GET /api/users/{userId} - get user by username
* POST /api/users - Create a new user
* PUT /api/users - Modify a user
* DELETE /api/users/{userId} - Delete user by username

#Users

The application comes with some pre-loaded users.

* username: user1, password: password, roles: PAGE_1
* username: user2, password: password, roles: PAGE_2
* username: user3, password: password, roles: PAGE_3
* username: admin, password: admin, roles: ADMIN

#Testing

Unit tests have been developed using junit and mockito. 
In order to test the application using a programing language different
to java, integration test are done with python. To run the python tests, 
you first need to install some python dependencies into your local python environment
by simply running ``pip install -r requirements.txt`. Note that you need to 
launch the java application before running these tests. If everything is correct, you should see this output:

```test_create_user_and_delete (__main__.TestRestAPI) ... ok
test_create_user_with_non_admin_user (__main__.TestRestAPI) ... ok
test_get_list_of_users (__main__.TestRestAPI) ... ok
test_get_user_that_does_not_exist (__main__.TestRestAPI) ... ok
test_update_user (__main__.TestRestAPI) ... ok
```
 
#How to run it

The project is build using maven. You can simply run ``mvn package`` and execute the
jar file created under the target directory with ``java -jar httpserver-1.0-SNAPSHOT.jar`
The server is started at port 8080.
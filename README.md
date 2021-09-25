# :spaghetti:	 Recipe API Backend
![Java](https://img.shields.io/badge/-Java-000?&logo=Java&logoColor=007396)
![Docker](https://img.shields.io/badge/-Docker-000?&logo=Docker)
![Spring](https://img.shields.io/badge/-Spring-000?&logo=Spring)


## Description:
The Recipe API Backend allows performing following operation. Equipped with state of art  `Spring caching` for faster responses and :closed_lock_with_key:	 `Stateless REST JWT based Bearer token` authentication support with containerization suport with :whale: 	`Dockerfile`.
- Create Recipe
- Edit created recipe
- Delete created recipes
- Retrieve all recipes with pagination support
- Retrieve recipe with detailed instruction
- Create User 
- Login with user details

## :shield:	 Code coverage
![Screenshot 2021-09-23 at 09 18 24](https://user-images.githubusercontent.com/14979620/134470003-aee1b61a-8860-43e4-b1bf-8108693e60a3.png)

## :crossed_fingers:	Synk vulnerability scan report for dependancy and code
![Screenshot 2021-09-23 at 13 05 03](https://user-images.githubusercontent.com/14979620/134496791-dbbd217e-90ff-423d-b7dc-06ca10910618.png)

![Screenshot 2021-09-23 at 13 05 17](https://user-images.githubusercontent.com/14979620/134496762-7120c892-f029-43d8-824c-de142fbd38a2.png)


## :balance_scale:	Assumptions :
- Validations for recipe creation and update are assumed as 
  - recipeName should be at least 5 characters
  - cuisine, course and instructionText should be at least 3 characters
  - serves,prepTime,cookingTime and instructionNumber should always be non-zero positive number
  - name,unit and quantity of ingredient should be not empty
- Validations for users
  - username and password should be at least 8 characters long.
- Only the user who created recipe can edit,update or delete the recipe

## :hammer_and_wrench:	Tech-Stack
![Java](https://img.shields.io/badge/-Java-000?&logo=Java&logoColor=007396)
![Docker](https://img.shields.io/badge/-Docker-000?&logo=Docker)
![Spring](https://img.shields.io/badge/-Spring-000?&logo=Spring)

- Java 8 
- Spring-Boot
- JPA
- In-Memory Database H2
- Maven
- Git bash

## :memo: Steps to run the application
- Checkout the code / Download from git repo()
- checkout : open git bash and run command `git clone https://github.com/Abhinav2510/recipe.git`
- Option 1: Maven way of running
  - open command prompt(cmd) or terminal on Mac
  - navigate to the project folder
  - run command `mvn clean install`
  - once its successfully build run command `mvn spring-boot: run`
- Option 2: Docker way of running
  - Open terminal and run `docker-build-run.sh`

Now application is up and running on http://localhost:8080

## :grey_question:	How to use this service
- Open the URL in your browser : http://localhost:8080
- User will see a swagger page with all the defined specs of the service.
- There will have 2 Tags you can see.


### 1. user-controller
#### Description:
- Endpoint 1: `POST /users/signup`
  - Allows creation of user
- Endpoint 2: `POST /users/signin`
  - Allows user to login
  - On providing correct credential in request the response provides Bearer token in header which can be used for calling further API

### 2. recipe-controller
#### Description:
- All the below endpoints are secured with stateless JWT authentication.
- All request to below Endpoints should contain custom header `authorization` with value containing `Bearer {JWT}`
- Endpoint 1:  `POST /recipe/create`
    - Allows creation of recipes with above-mentioned validations
- Endpoint 2: `GET /recipe/{recipeId}`
    - Response contains detailed recipe with all instructions and ingredients as part of recipe
- Endpoint 3: `PUT /recipe/{recipeId}`
  - Allows update of existing recipe with above-mentioned validations
  - Only the creator user can Update recipe
- Endpoint 4: `DELETE /recipe/{recipeId}`
  - Allows deletion of recipe created previously 
  - Only the creator user can delete recipe
- Endpoint 5: `GET /recipe/`
    - Gets all the recipes with pagination support with default page size 0
    

### :test_tube: Testing using Swagger UI

####Running application
- Run application using `mvn spring-boot: run` or `java -jar /target/recipe-0.0.1-SNAPSHOT.jar`
- Navigate to http://localhost:8080
- ![Screenshot 2021-09-23 at 00 33 45](https://user-images.githubusercontent.com/14979620/134431549-93e3718e-85c5-4f24-970c-2f4998d0c337.png)


#### Authenticating for using Recipe API
- under `user-controller` tab you can create user or use already created user to authenticate 
- to create user use `/users/signup` endpoint
- to Use default user for application use below JSON object for `user/signin`
```
  {
  "userName":"TestUser1",
  "password":"password1234"
  } 
```
- It will return response header `authorization` with JWT token
- ![Screenshot 2021-09-23 at 00 35 49](https://user-images.githubusercontent.com/14979620/134431617-56e89f8d-5c41-4daf-bf00-cae108677c86.png)
- Copy the value in `authorization` header
- On top left corner click on Authorize button and enter copied value
- ![Screenshot 2021-09-23 at 00 37 57](https://user-images.githubusercontent.com/14979620/134431583-a86083de-afed-4d23-b2b9-ff5440c51a30.png)


Now you should be able to call all the APIs without needing to specify `authorization` header manually 


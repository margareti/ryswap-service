Feature: Create new user account

 Scenario: User is able to create an account
    Given a sign up request for "penio@test.com" with password "password" and name "Penio"
    When I post the request to "/api/registerUser"
    Then the server responds with "201"
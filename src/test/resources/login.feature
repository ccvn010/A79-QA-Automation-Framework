Feature: Login

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter email "calvinqnguyen07@gmail.com" and password "CalvinLP-Zani69__"
    And I click the login button
    Then I should be redirected to the home page

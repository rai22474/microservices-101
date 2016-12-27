Feature: Create customer

  As a Ari administrator
  In order to add new customer to the system
  I want to create new customer


  @deleteCustomer
  Scenario: Create customer
    When i try to create a new customer with with code "ES0182000003081"
    Then the response must be "CREATED"
    And the response have the identifier of the customer

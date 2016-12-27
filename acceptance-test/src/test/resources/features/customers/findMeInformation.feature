# language: en

@deleteCustomer
Feature: Retrieve me information

  As a Customer
  In order to know the information stored to me in the system
  I want query me information

  Scenario: Find data for the logged client
    Given an bunch of customers with identity cards "94465684S,Y8197160C,P4544208D,42854637W"
    And the customer "94465684S" is correct logged
    When i request the current customer data
    Then the response must be "OK"
    And have the customer common data
    And the response has links to
      | link                        |
      | bucks                       |
      | movements                   |
      | wallet                      |
      | settings                    |
      | createCard                  |
      | createMoneyOrder            |
      | createMoneyOrderDraft       |
      | createMoneyRequest          |
      | createMoneyRequestDraft     |
      | editMe                      |
      | editSettings                |

  Scenario: Return not found when find the user data when logged user don't exists
    Given an bunch of customers with identity cards "94465684S,Y8197160C,P4544208D,42854637W"
    And the customer "94465684S" is correct logged but don't exists in the active customers
    When i request the current customer data
    Then the response must be "NOT_FOUND"

  Scenario: Return bad request if no customer id header is included
    When i request the customer data without costumer header
    Then the response must be "BAD_REQUEST"
    
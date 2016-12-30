# language: en
@deleteCustomer
Feature: Customer delete

  As a customer,
  I want to be able to delete my account

  Background:
    Given a customers with the following data:
      | id      | identityCard | name   | lastName  | email                           | phone     | avatar  |
      | abcdefg | 79986535X    | Alfred | Hitchcock | alfred.hitchcock@indiscreet.com | 636561241 | potatoe |

  Scenario: Delete customer happy path
    When customer "79986535X" deletes his account:
    Then the response must be "NO_CONTENT"
    And the customer "79986535X" account is deleted from the system:
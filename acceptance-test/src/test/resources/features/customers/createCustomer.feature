# language: en
@deleteCustomer
Feature: Customer registration

  As a customer,
  I need to register on the application to create my own account and have a mechanism to log into my own account

  Background:
    Given a customers with the following data:
      | id      | identityCard | name   | lastName  | email                           | phone     | avatar  |
      | abcdefg | 79986535X    | Alfred | Hitchcock | alfred.hitchcock@indiscreet.com | 636561241 | potatoe |

  Scenario: Register a new person successfully
    When a person is registered in the system with the following data:
      | id                  | name | lastName | idCard    | email         | termsAndConditions | mobilePhone |
      | 43r34t-rgertwe-rw43 | Bart | Simpson  | 50861048K | lmb@gmail.com | true               | 636589713   |
    Then the response must be "CREATED"
    And the response has links to
      | link | api        | href   |
      | self | ari-read | api/me |
    And therefore, it exists a customer, identified by "43r34t-rgertwe-rw43" with the following data:
      | name | lastName | idCard    | email         |
      | Bart | Simpson  | 50861048K | lmb@gmail.com |
    And the customer "50861048K" has a card with the following data:
      | type | bankingServiceCardId | pan              | image            | name | lastName |
      | tva  | 9afj98asdf           | 400000******2419 | virtual-card.png | Bart | Simpson  |

  Scenario: Register a new person and verifying its bucks limits
    When a person is registered in the system with the following data:
      | id                  | name | lastName | idCard    | email         | termsAndConditions | mobilePhone |
      | 43r34t-rgertwe-rw43 | Bart | Simpson  | 50861048K | lmb@gmail.com | true               | 636589713   |
    Then the response must be "CREATED"
    And the customer "50861048K" has the following limits into its bucks:
      | rechargeThisPeriod | rechargeLast | rechargeRemaining | rechargeMax |
      | 0.0 EUR            | 0.0 EUR      | 2500.0 EUR        | 2500.0 EUR  |

  Scenario: Conflict when a customer exists with the given identifier
    When a person is registered in the system with the following data:
      | id      | name | lastName | idCard    | email         | termsAndConditions |
      | abcdefg | Bart | Simpson  | 50861048K | lmb@gmail.com | true               |
    Then the response must be "CONFLICT"
    And the response has the following data
      | code               | description                                                         |
      | existingIdentifier | Attempting to create a customer with an existing identifier:abcdefg |

  Scenario: Conflict when a customer exists with the given idCard
    When a person is registered in the system with the following data:
      | id         | name | lastName | idCard    | email         | termsAndConditions |
      | lahdsfalsd | Bart | Simpson  | 79986535X | lmb@gmail.com | true               |
    Then the response must be "CONFLICT"
    And the response has the following data
      | code           | description                                                       |
      | existingIdCard | Attempting to create a customer with an existing idCard:79986535X |

  Scenario: Conflict when creating a customer with an invalid idCard
    When a person is registered in the system with the following data:
      | id         | name | lastName | idCard    | email         | termsAndConditions |
      | lahdsfalsd | Bart | Simpson  | 12345678P | lmb@gmail.com | true               |
    Then the response code must be "422"
    And the response has the following data
      | code          | description                                         |
      | invalidIdCard | Attempting to create a customer with invalid idCard |

  Scenario: Conflict when a customer exists with the given mobilePhone
    When a person is registered in the system with the following data:
      | id         | name | lastName | idCard    | email         | termsAndConditions | mobilePhone |
      | lahdsfalsd | Bart | Simpson  | 50861048K | lmb@gmail.com | true               | 636561241   |
    Then the response must be "CONFLICT"
    And the response has the following data
      | code                | description                                                             |
      | existingMobilePhone | Attempting to create a customer with an existing mobile phone:636561241 |

  Scenario Outline: Bad request when not sending required properties
    When a person is registered in the system with the following properties:
      | id         | name   | lastName   | idCard   | email   | mobilePhone   | termsAndConditions   | mobilePhone |
      | lahdsfalsd | <name> | <lastName> | <idCard> | <email> | <mobilePhone> | <termsAndConditions> | 777777777   |
    Then the response must be "<responseStatus>"

    Examples:
      | name  | lastName | idCard | email | mobilePhone | termsAndConditions | responseStatus |
      | false | true     | true   | true  | true        | true               | BAD_REQUEST    |
      | true  | false    | true   | true  | true        | true               | BAD_REQUEST    |
      | true  | true     | false  | true  | true        | true               | BAD_REQUEST    |
      | true  | true     | true   | false | true        | true               | BAD_REQUEST    |
      | true  | true     | true   | true  | true        | false              | BAD_REQUEST    |
      | true  | true     | true   | true  | true        | true               | CREATED        |


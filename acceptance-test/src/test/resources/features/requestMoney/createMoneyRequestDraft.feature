# language: en
@deleteCustomer @deleteMoneyRequestBundle
Feature: Create a draft of a money request to other person

  As customer
  In order to prepare a money request
  I want to create a draft of a money request

  Background:
    Given a customers with the following data:
      | identityCard | name    | lastName  | email                           | phone     | avatar   | bankingServiceAgreementId |
      | 79986535X    | Alfred  | Hitchcock | alfred.hitchcock@indiscreet.com | 636561241 | potatoe  | 1a                        |
      | Y8197160C    | Stanley | Kubric    | kubric@me.com                   | 685551441 | broccoli | 2b                        |
      | 32971623L    | Clint   | Eastwood  | highway@me.com                  | 68556241  | carrot   | 3c                        |
    And the customer "79986535X" has the following money savings kept in the system:
      | recharge |
      | 60.3 EUR |
    And the customer "Y8197160C" has the following money savings kept in the system:
      | recharge  |
      | 2960.3 EUR |
    And the customer "32971623L" has the following money savings kept in the system:
      | recharge  |
      | 620.3 EUR |

  Scenario: Create a money request draft with integer amount to other wizzo customer
    When customer "79986535X" creates a request draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "take the money and run"
    Then the response must be "CREATED"
    And the money request draft doesn't have any errors
    And the money request draft has the following data:
      | reason                 | totalAmount |
      | take the money and run | 10.0 EUR      |

  Scenario: Create a money request draft when sender cannot receive that amount of money
    When customer "Y8197160C" creates a request draft with "300 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice"
    Then the response must be "CREATED"
    And the returned money request bundle must have the following errors
      | code                   | description                                        |
      | NotEnoughRechargeLimit | Bucks doesn't have enough remaining recharge limit |

  Scenario: Create a money request draft when sender is the same as the recipient
    When customer "Y8197160C" creates a request draft with "10 EUR" to other customer with identifier "Y8197160C" with reason "the wedding invoice"
    Then the response must be "CREATED"
    And the returned money request bundle must have the following errors
      | code                 | description                      |
      | InvalidMoneyRequests | The money requests are not valid |
    And the returned money request must have the following errors
      | code                  | description                               |
      | SameRecipientAsSender | The recipient is the same than the sender |

  Scenario: Create a money request draft when sender tva is blocked
    Given the customer "Y8197160C" with the tva card blocked
    When customer "Y8197160C" creates a request draft with "10 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice"
    Then the response must be "CREATED"
    And the returned money request bundle must have the following errors
      | code         | description        |
      | BlockedBucks | The tva is blocked |

  Scenario Outline: Bad request when not sending required properties
    When customer "32971623L" creates a money request draft with the following properties
      | reason   | moneyRequests   | amount   | currency   | value   | name   | mobilePhone   | email   | unknownProperty   |
      | <reason> | <moneyRequests> | <amount> | <currency> | <value> | <name> | <mobilePhone> | <email> | <unknownProperty> |
    Then the response must be "<responseStatus>"

    Examples:
      | reason | moneyRequests | amount | currency | value | name  | mobilePhone | email | unknownProperty | responseStatus |
      | false  | false         | false  | false    | false | false | false       | false | false           | CREATED        |
      | true   | false         | false  | false    | false | false | false       | false | false           | CREATED        |
      | false  | true          | true   | true     | true  | true  | false       | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | true  | false | true        | false | false           | BAD_REQUEST    |
      | false  | true          | false  | true     | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true          | true   | false    | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | false | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | true  | true  | true        | false | false           | CREATED        |
      | true   | true          | true   | true     | true  | true  | true        | true  | true            | CREATED        |

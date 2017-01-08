# language: en
@deleteCustomer @deleteMoneyRequestBundle @deleteMovement
Feature: Request money to other customer

  As customer
  In order to increase my funds
  I want to request money to other people

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
      | 2860.3 EUR |
    And the customer "32971623L" has the following money savings kept in the system:
      | recharge  |
      | 620.3 EUR |

  Scenario: Request money to a customer
    When customer "79986535X" requests "10.0 EUR" to other customer with identifier "Y8197160C" with reason "take the money and run"
    Then the response must be "ACCEPTED"
    And the money request has the following data:
      | reason                 | totalAmount |
      | take the money and run | 10.0 EUR    |
    And the money request must have generated a movement for customer id "79986535X"
    And the movement has "agree|createdOn"
    And the movement has the following data
      | movementType       | reason                 | amount |
      | moneyRequestBundle | take the money and run | 10.0 EUR |
    And the movement has the following "moneyRequests" data:
      | amount |
      | 10 EUR |
    And there is a movement for the customer "Y8197160C" of type "moneyRequestReception"

  Scenario: The customer that receive a request has a movement
    When customer "79986535X" requests "10.0 EUR" to other customer with identifier "Y8197160C" with reason "give me some money"
    Then the response must be "ACCEPTED"
    And the money request has the following data:
      | reason             | totalAmount |
      | give me some money | 10.0 EUR    |
    And the money request must have generated a movement for customer id "79986535X"
    And the movement has the following data
      | movementType       | reason             | amount |
      | moneyRequestBundle | give me some money | 10 EUR |
    And there is a movement for the customer "Y8197160C" of type "moneyRequestReception"
    And the movement has "agree|createdOn"
    And the movement has "to" with the following data:
      | participantType | me   |
      | person          | true |
    And the movement has "from" with the following data:
      | participantType | name   | lastName  |
      | person          | Alfred | Hitchcock |

  Scenario: Send momey request without reason generated movements
    When customer "79986535X" requests "10.0 EUR" to other customer with identifier "Y8197160C" with no reason
    Then the response must be "ACCEPTED"
    And the money request must have generated a movement for customer id "79986535X"
    And the money request has the following data:
      | totalAmount |
      | 10.0 EUR    |
    And there is a movement for the customer "Y8197160C" of type "moneyRequestReception"
    And the movement has "agree|createdOn"
    And the movement has "to" with the following data:
      | participantType | me   |
      | person          | true |
    And the movement has "from" with the following data:
      | participantType | name   | lastName  |
      | person          | Alfred | Hitchcock |

  Scenario: Conflict when request money to a customer and the sender cannot receive that amount of money
    When customer "Y8197160C" requests "300 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice"
    Then the response must be "CONFLICT"
    And the returned money request bundle must have the following errors
      | code                   | description                                        |
      | NotEnoughRechargeLimit | Bucks doesn't have enough remaining recharge limit |

  Scenario: Conflict when request money to the same customer
    When customer "Y8197160C" requests "10 EUR" to other customer with identifier "Y8197160C" with reason "the wedding invoice"
    Then the response must be "CONFLICT"
    And the returned money request bundle must have the following errors
      | code                 | description                      |
      | InvalidMoneyRequests | The money requests are not valid |
    And the returned money request must have the following errors
      | code                  | description                               |
      | SameRecipientAsSender | The recipient is the same than the sender |

  Scenario: Conflict when the sender has the tva in status blocked
    Given the customer "Y8197160C" with the tva card blocked
    When customer "Y8197160C" requests "10 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice"
    Then the response must be "CONFLICT"
    And the returned money request bundle must have the following errors
      | code         | description        |
      | BlockedBucks | The tva is blocked |

  Scenario Outline: Bad request when not sending required properties
    When customer "32971623L" creates a money request with the following properties
      | reason   | moneyRequests   | amount   | currency   | value   | name   | mobilePhone   | email   | unknownProperty   |
      | <reason> | <moneyRequests> | <amount> | <currency> | <value> | <name> | <mobilePhone> | <email> | <unknownProperty> |
    Then the response must be "<responseStatus>"

    Examples:
      | reason | moneyRequests | amount | currency | value | name  | mobilePhone | email | unknownProperty | responseStatus |
      | false  | false         | false  | false    | false | false | false       | false | false           | BAD_REQUEST    |
      | true   | false         | false  | false    | false | false | false       | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | true  | true  | false       | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | true  | false | true        | false | false           | BAD_REQUEST    |
      | false  | true          | false  | true     | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true          | true   | false    | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | false | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true          | true   | true     | true  | true  | true        | false | false           | ACCEPTED       |
      | true   | true          | true   | true     | true  | true  | true        | true  | true            | ACCEPTED       |
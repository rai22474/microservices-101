# language: en
@deleteCustomer @deleteMoneyOrderBundle
Feature: Create draft of a money order to other customer

  As customer
  In order to prepare a money order
  I want to prepare a draft of a money order

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
      | recharge |
      | 2860.3 EUR |
    And the customer "32971623L" has the following money savings kept in the system:
      | recharge |
      | 620.3 EUR |

  Scenario: Draft money order to other customer
    When customer "79986535X" create a order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "take the money and run"
    Then the response must be "CREATED"
    And there are a money order draft without errors
    And the money order draft has the following data:
      | reason                 | amount | name    | mobilePhone |
      | take the money and run | 10.0 EUR | Stanley | 768723891   |
    And the money order has a recipient with the following data:
      | mobilePhone | name    |
      | 685551441   | Stanley |
    And the response has links to
      | link                |
      | editMoneyOrderDraft |

  Scenario: Draft money order with integer amount to other  customer
    When customer "79986535X" create a order draft with "10 EUR" to other customer with identifier "Y8197160C" with reason "take the money and run"
    Then the response must be "CREATED"
    And there are a money order draft without errors
    And the money order draft has the following data:
      | reason                 | amount |
      | take the money and run | 10.0 EUR |

  Scenario: The total amount of the order draft exceeds sender's available money
    When customer "79986535X" create a order draft with "700.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment"
    Then the response must be "CREATED"
    And the returned money order bundle must have the following errors
      | code               | description                         |
      | NotEnoughAvailable | Bucks doesn't have enough available |

  Scenario: The sender tva is blocked
    Given the customer "79986535X" with the tva card blocked
    When customer "79986535X" create a order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment"
    Then the response must be "CREATED"
    And the returned money order bundle must have the following errors
      | code         | description        |
      | BlockedBucks | The tva is blocked |

  Scenario: The order draft exceeds recipient limits
    When customer "32971623L" create a order draft with "300.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment"
    Then the response must be "CREATED"
    And the returned money order bundle must have the following errors
      | code               | description                    |
      | InvalidMoneyOrders | The money orders are not valid |
    And the returned money order must have the following errors
      | code                   | description                              |
      | RecipientCannotReceive | Recipient cannot receive the money order |

  Scenario: The order draft has the same recipient than the sender
    When customer "32971623L" create a order draft with "30.0 EUR" to other customer with identifier "32971623L" with reason "the wedding payment"
    Then the response must be "CREATED"
    And the returned money order bundle must have the following errors
      | code               | description                    |
      | InvalidMoneyOrders | The money orders are not valid |
    And the returned money order must have the following errors
      | code                  | description                               |
      | SameRecipientAsSender | The recipient is the same than the sender |

  Scenario: The recipient tva is blocked
    Given the customer "32971623L" with the tva card blocked
    When customer "32971623L" create a order draft with "30.0 EUR" to other customer with identifier "32971623L" with reason "the wedding payment"
    Then the response must be "CREATED"
    And the returned money order bundle must have the following errors
      | code               | description                    |
      | InvalidMoneyOrders | The money orders are not valid |
    And the returned money order must have the following errors
      | code                   | description                              |
      | RecipientCannotReceive | Recipient cannot receive the money order |

  Scenario Outline: Bad request when not sending required properties
    When customer "32971623L" creates a money order draft with the following properties
      | reason   | moneyOrders   | amount   | currency   | value   | name   | mobilePhone   | email   | unknownProperty   |
      | <reason> | <moneyOrders> | <amount> | <currency> | <value> | <name> | <mobilePhone> | <email> | <unknownProperty> |
    Then the response must be "<responseStatus>"

    Examples:
      | reason | moneyOrders | amount | currency | value | name  | mobilePhone | email | unknownProperty | responseStatus |
      | false  | false       | false  | false    | false | false | false       | false | false           | CREATED        |
      | true   | false       | false  | false    | false | false | false       | false | false           | CREATED        |
      | false  | true        | true   | true     | true  | true  | false       | false | false           | BAD_REQUEST    |
      | false  | true        | true   | true     | true  | false | true        | false | false           | BAD_REQUEST    |
      | false  | true        | false  | true     | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true        | true   | false    | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true        | true   | true     | false | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true        | true   | true     | true  | true  | true        | false | false           | CREATED        |
      | true   | true        | true   | true     | true  | true  | true        | true  | true            | CREATED        |
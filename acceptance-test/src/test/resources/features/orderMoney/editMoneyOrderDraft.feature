# language: en
@deleteCustomer @deleteMoneyOrderBundle
Feature: Edit an existing money order draft

  As customer
  In order to prepare a money order
  I want to prepare a draft of a money order

  Background:
    Given a customers with the following data:
      | identityCard | name      | lastName  | email                           | phone     | avatar    | bankingServiceAgreementId |
      | 79986535X    | Alfred    | Hitchcock | alfred.hitchcock@indiscreet.com | 636561241 | potatoe   | 1a                        |
      | Y8197160C    | Stanley   | Kubric    | kubric@me.com                   | 685551541 | broccoli  | 2b                        |
      | 32971623L    | Clint     | Eastwood  | highway@me.com                  | 68556241  | carrot    | 3c                        |
      | 89320575Y    | Bob Dylan | Dylan     | bob@dylan.com                   | 685551441 | romanesco | 4d                        |
    And the customer "79986535X" has the following money savings kept in the system:
      | recharge |
      | 60.3 EUR |
    And the customer "Y8197160C" has the following money savings kept in the system:
      | recharge  |
      | 2960.3 EUR |
    And the customer "32971623L" has the following money savings kept in the system:
      | recharge  |
      | 620.3 EUR |
    And the customer "89320575Y" has the following money savings kept in the system:
      | recharge  |
      | 840.3 EUR |

  Scenario: Update an existing draft
    Given an order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "79986535X"
    When the customer "79986535X" updates the money order draft, sending "200.0 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And there are a money order draft without errors
    And the money order draft has the following data:
      | reason      | amount  |
      | by the face | 200.0 EUR |

  Scenario: The total amount of the order draft exceeds sender's available money
    Given an order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "79986535X"
    When the customer "79986535X" updates the money order draft, sending "700.0 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And the returned money order bundle must have the following errors
      | code               | description                         |
      | NotEnoughAvailable | Bucks doesn't have enough available |

  Scenario: The sender tva is blocked
    Given an order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "79986535X"
    And the customer "79986535X" with the tva card blocked
    When the customer "79986535X" updates the money order draft, sending "700.0 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And the returned money order bundle must have the following errors
      | code         | description        |
      | BlockedBucks | The tva is blocked |

  Scenario: The amount of the order draft exceeds recipient recharge limit
    Given an order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "32971623L"
    When the customer "32971623L" updates the money order draft, sending "300.0 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And the returned money order bundle must have the following errors
      | code               | description                    |
      | InvalidMoneyOrders | The money orders are not valid |
    And the returned money order must have the following errors
      | code                   | description                              |
      | RecipientCannotReceive | Recipient cannot receive the money order |

  Scenario: The recipient tva is blocked
    Given an order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "32971623L"
    And the customer "32971623L" with the tva card blocked
    When the customer "32971623L" updates the money order draft, sending "300.0 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And the returned money order bundle must have the following errors
      | code               | description                    |
      | InvalidMoneyOrders | The money orders are not valid |
    And the returned money order must have the following errors
      | code                   | description                              |
      | RecipientCannotReceive | Recipient cannot receive the money order |

  Scenario: The recipient is the same as the sender
    Given an order draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "32971623L"
    When the customer "32971623L" updates the money order draft, sending "20 EUR" to the customer "32971623L" with reason "by the face"
    Then the response must be "OK"
    And the returned money order bundle must have the following errors
      | code               | description                    |
      | InvalidMoneyOrders | The money orders are not valid |
    And the returned money order must have the following errors
      | code                  | description                               |
      | SameRecipientAsSender | The recipient is the same than the sender |

  Scenario Outline: Bad request when not sending required properties
    Given an order draft with "15.0 EUR" to other customer with identifier "89320575Y" with reason "the wedding payment", created by the customer "32971623L"
    When customer "32971623L" updates the money order draft with the following properties
      | reason   | moneyOrders   | amount   | currency   | value   | name   | mobilePhone   | email   | unknownProperty   |
      | <reason> | <moneyOrders> | <amount> | <currency> | <value> | <name> | <mobilePhone> | <email> | <unknownProperty> |
    Then the response must be "<responseStatus>"

    Examples:
      | reason | moneyOrders | amount | currency | value | name  | mobilePhone | email | unknownProperty | responseStatus |
      | false  | false       | false  | false    | false | false | false       | false | false           | OK             |
      | true   | false       | false  | false    | false | false | false       | false | false           | OK             |
      | false  | true        | true   | true     | true  | true  | false       | false | false           | BAD_REQUEST    |
      | false  | true        | true   | true     | true  | false | true        | false | false           | BAD_REQUEST    |
      | false  | true        | false  | true     | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true        | true   | false    | true  | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true        | true   | true     | false | true  | true        | false | false           | BAD_REQUEST    |
      | false  | true        | true   | true     | true  | true  | true        | false | false           | OK             |
      | true   | true        | true   | true     | true  | true  | true        | true  | true            | OK             |
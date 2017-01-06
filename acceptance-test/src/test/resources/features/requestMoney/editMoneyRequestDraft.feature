# language: en
@deleteCustomer @deleteMoneyRequestBundle
Feature: Edit an existing draft of a money request to other person

  As a customer
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
      | 60.3 EUR  |
    And the customer "Y8197160C" has the following money savings kept in the system:
      | recharge |
      | 2800.3 EUR |
    And the customer "32971623L" has the following money savings kept in the system:
      | recharge |
      | 620.3 EUR |

  Scenario: Update an existing request draft
    Given a request draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "79986535X"
    When the customer "79986535X" updates the money request draft, requesting "200.0 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And the money request draft doesn't have any errors
    And the money request draft has the following data:
      | reason      | totalAmount |
      | by the face | 200.0 EUR     |

  Scenario: Update an existing request draft when sender cannot receive that amount of money
    Given a request draft with "200 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice", created by the customer "Y8197160C"
    When the customer "Y8197160C" updates the money request draft, requesting "300 EUR" to the customer "32971623L" with reason "by the face"
    Then the response must be "OK"
    And the returned money request bundle must have the following errors
      | code                   | description                                        |
      | NotEnoughRechargeLimit | Bucks doesn't have enough remaining recharge limit |

  Scenario: Update an existing request draft with the same sender and recipient
    Given a request draft with "200 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice", created by the customer "Y8197160C"
    When the customer "Y8197160C" updates the money request draft, requesting "10 EUR" to the customer "Y8197160C" with reason "by the face"
    Then the response must be "OK"
    And the returned money request bundle must have the following errors
      | code                 | description                      |
      | InvalidMoneyRequests | The money requests are not valid |
    And the returned money request must have the following errors
      | code                  | description                               |
      | SameRecipientAsSender | The recipient is the same than the sender |

  Scenario: Update an existing request draft when sender tva is blocked
    Given a request draft with "10 EUR" to other customer with identifier "32971623L" with reason "the wedding invoice", created by the customer "Y8197160C"
    And the customer "Y8197160C" with the tva card blocked
    When the customer "Y8197160C" updates the money request draft, requesting "10 EUR" to the customer "32971623L" with reason "by the face"
    Then the response must be "OK"
    And the returned money request bundle must have the following errors
      | code         | description        |
      | BlockedBucks | The tva is blocked |


  Scenario Outline: Bad request when not sending required properties
    Given a request draft with "10.0 EUR" to other customer with identifier "Y8197160C" with reason "the wedding payment", created by the customer "32971623L"
    When customer "32971623L" edit a money request draft with the following properties
      | reason   | moneyRequests   | amount   | currency   | value   |  name  | mobilePhone   | email   | unknownProperty   |
      | <reason> | <moneyRequests> | <amount> | <currency> | <value> | <name> | <mobilePhone> | <email> | <unknownProperty> |
    Then the response must be "<responseStatus>"

  Examples:
    | reason | moneyRequests | amount | currency   | value | name  | mobilePhone | email | unknownProperty | responseStatus |
    | false  | false         | false  | false      | false | false | false       | false | false           | OK             |
    | true   | false         | false  | false      | false | false | false       | false | false           | OK             |
    | false  | true          | true   | true       | true  | true  | false       | false | false           | BAD_REQUEST    |
    | false  | true          | true   | true       | true  | false | true        | false | false           | BAD_REQUEST    |
    | false  | true          | false  | true       | true  | true  | true        | false | false           | BAD_REQUEST    |
    | false  | true          | true   | false      | true  | true  | true        | false | false           | BAD_REQUEST    |
    | false  | true          | true   | true       | false | true  | true        | false | false           | BAD_REQUEST    |
    | false  | true          | true   | true       | true  | true  | true        | false | false           | OK             |
    | true   | true          | true   | true       | true  | true  | true        | true  | true            | OK             |

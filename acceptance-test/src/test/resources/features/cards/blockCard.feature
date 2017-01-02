# language: en
@deleteCustomer @deleteCard
Feature: Block the tva card

  As customer
  In order to report that tva was stolen
  I want to block my tva card

  Background:
    Given a customers with the following data:
      | id      | identityCard | name    | lastName  | email                           | phone     | avatar   | bankingServiceAgreementId | bankingServiceCardId |
      | abcdefg | 79986535X    | Alfred  | Hitchcock | alfred.hitchcock@indiscreet.com | 636561241 | potatoe  | a78d96b8a5db86a864        | 94ad7ba4dad93        |
      | zyxwvut | Y8197160C    | Stanley | Kubric    | kubric@me.com                   | 685551441 | broccoli | 2b                        | a4bdb4b4ab4da        |

  Scenario: Block tva happy path
    When the customer "79986535X" blocks its tva card
    Then the response must be "CREATED"
    And the response has links to
      | link | api        |
      | self | ari-read |
    And the customer "79986535X" "tva" card status has changed to "blocked"

  Scenario: Conflict when the tva is already blocked
    And the customer "79986535X" with the tva card blocked
    When the customer "79986535X" blocks its tva card
    Then the response must be "CONFLICT"

  Scenario: Block a non-existing card
    When the customer "79986535X" blocks the card "8bada5d8ad8a9bd"
    Then the response must be "NOT_FOUND"

  @pending
  Scenario: Block a card from another customer
    When the customer "79986535X" blocks the last created card
    Then the response must be "NOT_FOUND"

  @pending
  Scenario: Block a card by an non-existing customer
    When an unknown customer blocks the last created card
    Then the response must be "NOT_FOUND"
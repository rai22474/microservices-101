@deleteCustomer
Feature: Customer edits his information

  As a customer,
  I want to update my profile

  Background:
    Given a customers with the following data:
      | id      | identityCard | name   | lastName  | email                           | phone     | avatar  |
      | abcdefg | 79986535X    | Alfred | Hitchcock | alfred.hitchcock@indiscreet.com | 636561241 | potatoe |

  Scenario: a customer enters his address for the first time
    When the customer "79986535X" updates his address to:
      | addressType | streetAddress | streetNumber | houseNumber | houseLetter | postcode | town   | country |
      | avenida     | Maldonado     | 2            | 4           | B           | 28000    | Madriz | Spain   |
    Then the response must be "NO_CONTENT"
    And the customer "79986535X" has his address updated to
      | addressType | streetAddress | streetNumber | houseNumber | houseLetter | postcode | town   | country |
      | avenida     | Maldonado     | 2            | 4           | B           | 28000    | Madriz | Spain   |

  Scenario: a customer updates his previous address
    Given the customer "79986535X" has the following address in wizzo:
      | addressType | streetAddress | streetNumber | houseNumber | houseLetter | postcode | town      | country   |
      | calle       | Elm           | 1            | 3           | A           | 28006    | Barcelona | Catalonia |
    When the customer "79986535X" updates his address to:
      | addressType | streetAddress | streetNumber | houseNumber | houseLetter | postcode | town   | country |
      | avenida     | Maldonado     | 2            | 4           | B           | 28000    | Madriz | Spain   |
    Then the response must be "NO_CONTENT"
    And the customer "79986535X" has his address updated to
      | addressType | streetAddress | streetNumber | houseNumber | houseLetter | postcode | town   | country |
      | avenida     | Maldonado     | 2            | 4           | B           | 28000    | Madriz | Spain   |

  Scenario: a customer updates his name and last name
    When the customer "79986535X" updates his name to "Pepe" and last name to "Perez"
    Then the response must be "NO_CONTENT"
    And the customer "79986535X" has his name and last name updated to "Pepe" "Perez"

  Scenario: a customer updates his email
    When the customer "79986535X" updates his email to "new@email.com"
    Then the response must be "NO_CONTENT"
    And the customer "79986535X" has his email updated to "new@email.com"

  Scenario Outline: Bad request when not sending required properties
    When the customer "79986535X" update his data to:
      | address   | addressType   | streetAddress   | streetNumber   | postcode   | town   | country   | geolocation   | latitude   | longitude   | name   | lastName   | email   |
      | <address> | <addressType> | <streetAddress> | <streetNumber> | <postcode> | <town> | <country> | <geolocation> | <latitude> | <longitude> | <name> | <lastName> | <email> |
    Then the response must be "<responseStatus>"

  Examples:
    | address | addressType | streetAddress | streetNumber | postcode | town  | country | geolocation | latitude | longitude | name  | lastName | email | responseStatus |
    | false   | false       | false         | false        | false    | false | false   | false       | false    | false     | false | false    | false | BAD_REQUEST    |
    | true    | true        | true          | true         | true     | true  | true    | true        | true     | true      | false | false    | false | BAD_REQUEST    |
    | true    | true        | true          | true         | true     | true  | true    | true        | true     | true      | false | false    | false | BAD_REQUEST    |
    | true    | false       | false         | false        | false    | false | false   | true        | true     | false     | true  | true     | true  | BAD_REQUEST    |
    | true    | true        | true          | true         | true     | true  | true    | true        | false    | true      | true  | true     | true  | BAD_REQUEST    |
    | true    | false       | false         | false        | false    | false | false   | false       | false    | false     | true  | true     | true  | NO_CONTENT     |
    | true    | true        | true          | true         | true     | true  | true    | true        | true     | true      | true  | true     | true  | NO_CONTENT     |
    | false   | false       | false         | false        | false    | false | false   | false       | false    | false     | true  | false    | false | NO_CONTENT     |
    | false   | false       | false         | false        | false    | false | false   | false       | false    | false     | false | true     | false | NO_CONTENT     |
    | false   | false       | false         | false        | false    | false | false   | false       | false    | false     | false | false    | true  | NO_CONTENT     |

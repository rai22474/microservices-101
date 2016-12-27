# language: en

Feature: Create agreement 

As a infrastructure manager
In order to know is the server is alive 
I want to return a heartbeat

Scenario: Return heartbeat when request. 
    When i request a heart beat
    Then the response must be "OK"
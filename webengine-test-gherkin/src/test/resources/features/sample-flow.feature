Feature: Test du parcours

  Background:
    Given I visit the test page "https://axafrance.github.io/webengine-dotnet/demo/Test.html" for running journey
    When I click on the link Start step 1

  @flow
  Scenario: Step1
    And I choose the language with text "Français"
    And I want to buy a coffee
    And I click on the first next button
    And I write a comment like "Test comment"
    And I choose also horns monster feature
    And I click on the second the next button
    And I enter a date "11/01/2023"
    And I enter a password "azerty123456"
    And I click on the third the next button
    And I click on the OK button in the pop up after i'm done
    Then I see the Done title
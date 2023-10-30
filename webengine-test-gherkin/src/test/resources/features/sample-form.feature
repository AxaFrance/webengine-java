Feature: Test du formulaire dans la page d'accueil

  @flow1
  Scenario: Test du bouton OK
    Given I visit the test page "https://axafrance.github.io/webengine-dotnet/demo/Test.html"
    When I press on the OK button
    And I see a pop up
    And I click on the OK button in the pop up
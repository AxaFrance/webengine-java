Feature: Test du formulaire dans la page d'accueil

  Scenario: Test du bouton OK
    Given I visit the test page "http://webengine-test.azurewebsites.net/"
    When I press on the OK button
    And I see a pop up
    And I click on the OK button in the pop up
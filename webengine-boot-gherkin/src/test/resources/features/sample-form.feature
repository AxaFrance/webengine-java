Feature: Test du formulaire dans la page d'accueil

  Scenario: Test du bouton OK
    Given I visit the test page "http://webengine-test.azurewebsites.net/"
    When I press on the OK button
    And I see a pop up
    And I click on the OK button in the pop up



#  Scenario: Test du bouton Input
#    Given I visit the test page "http://webengine-test.azurewebsites.net/"
#    When I presse the Inout button
#    And I enter a text in the pop up
#    Then I see the text in the input text



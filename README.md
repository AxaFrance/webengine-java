# Command for run the project with desktop mode
java -jar WebRunner.jar "-a:C:\work\projet-git\solaris-automation-keyword-driven\target\solaris-automation-1.0-SNAPSHOT.jar"
                        "-data:C:\work\projet-git\Automatisation_Edecla_Front\src\main\resources\TEST_SUITE_RECETTE.xml"
                        "-env:C:\work\projet-git\Automatisation_Edecla_Front\src\main\resources\ENV_RECETTE.xml"
                        "-browser:ChromiumEdge"
                        -m
                        -showreport 

# Command for run the project with mobile mode
java -jar WebRunner.jar "-a:C:\work\projet-git\solaris-automation-keyword-driven\target\solaris-automation-1.0-SNAPSHOT.jar"
                        "-data:C:\work\projet-git\Automatisation_Edecla_Front\src\main\resources\TEST_SUITE_RECETTE.xml"
                        "-env:C:\work\projet-git\Automatisation_Edecla_Front\src\main\resources\ENV_RECETTE.xml"
                        "-platform:Android"
                        "-browser:Chrome"
                        -m
                        -showreport
You need to have the "application-properties.yml" in the resource directory
Exemple of this file :

application:
  name: axa-fr-automation
  platformName: IOS
  browserName: SAFARI
appiumSettings:
  gridConnection: https://hub-cloud.browserstack.com/wd/hub
  userName: XXXXXX
  password: XXXXXX
  localTesting:
    activate: 
    arguments:        
  capabilities:
    desiredCapabilitiesMap:
      deviceName: iPad Pro 12.9 2021
      osVersion: 14.5
      projectName: axa-fr-automation
      buildName: axa-fr-automation-mobile
      sessionName: iPad
      local: true
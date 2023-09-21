Installer la JDK avec le certificats AXA !

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
  platformName: ANDROID
  browserName: CHROME  
appiumSettings:
  gridConnection: https://hub-cloud.browserstack.com/wd/hub
  userName: XXXXXXX
  password: XXXXXXX
  localTestingConfiguration:
    activate: true
    arguments:
      force: true
      forcelocal: true
      binarypath: C:\\BrowserStack\\BrowserStackLocal.exe
      localIdentifier: XXXXYYYY
  capabilities:
    desiredCapabilitiesMap:
      geoLocation: FR => en fonction des applications
      deviceName: Samsung Galaxy S20 Ultra
      osVersion: 10.0
      projectName: e-decla-automation
      buildName: e-decla-automation-mobile
      sessionName: Samsung
      local: true
      networkLogs: true
      localIdentifier: XXXXYYYY


If you want ton run test with tag
mvn test -Dcucumber.filter.tags="@FirstStep"
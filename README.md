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
  name: E-decla
appiumSettings:
  gridConnection: http://localhost:4723/wd/hub
  userName:
  password:
  capabilities:
    deviceName:
    desiredCapabilitiesMap:
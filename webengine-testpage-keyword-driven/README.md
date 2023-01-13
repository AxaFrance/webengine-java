# Install example project with keyword driven

Import project from Azure  Devops
- __[link for clone project](https://axafrance.visualstudio.com/DefaultCollection/Automateam/_git/solaris-automation-keyword-driven)__ https://axafrance.visualstudio.com/DefaultCollection/Automateam/_git/solaris-automation-keyword-driven


## Configure maven in intellij or eclipse
Configure maven directory

Configure maven settings

run after the command : mvn clean install -U 

## Configure program arguments before running application with intellij or eclipse

"-data:C:\work\projet-git\solaris-automation-keyword-driven\src\main\resources\data.xml"

"-env:C:\work\projet-git\solaris-automation-keyword-driven\src\main\resources\env.xml"

"-browser:ChromiumEdge"

## Configure maven in intellij or eclipse
java -jar solaris-automation-keyword-driven.jar "-data:C:\work\projet-automatisation\solaris-automation-keyword-driven\src\main\resources\data.xml"
                                 "-env:C:\work\projet-automatisation\solaris-automation-keyword-driven\src\main\resources\env.xml"
                                 "-browser:ChromiumEdge"
                                 -m
                                 -showreport 

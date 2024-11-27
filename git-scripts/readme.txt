1-Go to the directory "axa-webengine-java" (main directory of project) and run the following command:

2- ./git-scripts/create-relesae-from-develop.sh <RELEASE_NUMBER>
This will create a new branch "release-<RELEASE_NUMBER>" from the develop branch and will push it to the remote repository.

3- ./git-scripts/create-preprod-from-release.sh <RELEASE_NUMBER>
This will create a new branch "preprod-<RELEASE_NUMBER>" from the release branch and will push it to the remote repository.

4- ./git-scripts/merge-release-to-master.sh <RELEASE_NUMBER>
This will merge the release branch to the master branch and will push it to the remote repository.

5- ./git-scripts/merge-release-to-develop.sh <RELEASE_NUMBER> <NEW_SNAPSHOT_VERSION>
This will merge the release branch to the develop branch and will push it to the remote repository.


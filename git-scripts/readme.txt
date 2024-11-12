1-Go to the directory "git-scripts" and run the following command:

2- ./create-relesae-from-develop.sh <RELEASE_NUMBER>
This will create a new branch "release-<RELEASE_NUMBER>" from the develop branch and will push it to the remote repository.

3- ./create-preprod-from-release.sh <RELEASE_NUMBER>
This will create a new branch "preprod-<RELEASE_NUMBER>" from the release branch and will push it to the remote repository.

4- ./merge-release-to-master.sh <RELEASE_NUMBER>
This will merge the release branch to the master branch and will push it to the remote repository.

5- ./merge-release-to-develop.sh <RELEASE_NUMBER> <NEW_SNAPSHOT_VERSION>
This will merge the release branch to the develop branch and will push it to the remote repository.


#!/bin/bash

# Variables
RELEASE_VERSION=$1
RELEASE_BRANCH="release/$RELEASE_VERSION"
MASTER_BRANCH="master"

# Function to update the version in pom.xml
update_version() {
  mvn versions:set -DnewVersion=$1
  mvn versions:update-child-modules
  mvn versions:commit
}

# save work
echo "==================================================================================="
echo "git stash push -u -m \"Stash changes before creating release branch\""
echo "==================================================================================="
git stash push -u -m "Stash changes before creating release branch"


# Checkout release branch
echo "==================================================================================="
echo "git checkout -f -B $RELEASE_BRANCH origin/$RELEASE_BRANCH"
echo "==================================================================================="
git checkout -f -B $RELEASE_BRANCH origin/$RELEASE_BRANCH


# Pull the latest changes
echo "==================================================================================="
echo "git pull origin $RELEASE_BRANCH"
echo "==================================================================================="
git pull origin $RELEASE_BRANCH


# save work
echo "==================================================================================="
echo "git stash push -u -m \"Stash changes before creating main branch\""
echo "==================================================================================="
git stash push -u -m "Stash changes before creating main branch"

# Checkout main branch
echo "==================================================================================="
echo "git checkout -f -B $MASTER_BRANCH origin/$MASTER_BRANCH"
echo "==================================================================================="
git checkout -f -B $MASTER_BRANCH origin/$MASTER_BRANCH

# Pull the latest changes
echo "==================================================================================="
echo "git pull origin $MASTER_BRANCH --allow-unrelated-histories"
echo "==================================================================================="
git pull origin $MASTER_BRANCH --allow-unrelated-histories

# Merge the release branch into main
echo "================================================================================================================================"
echo "git merge $RELEASE_BRANCH --strategy-option theirs -m \"Merge $RELEASE_BRANCH into $MASTER_BRANCH\" --allow-unrelated-histories"
echo "================================================================================================================================"
git merge $RELEASE_BRANCH --strategy-option theirs -m "Merge $RELEASE_BRANCH into $MASTER_BRANCH" --allow-unrelated-histories

# Update the version in pom.xml
echo "==================================================================================="
echo "update_version $RELEASE_VERSION"
echo "==================================================================================="
update_version $RELEASE_VERSION

# Add the changes to the index
echo "==================================================================================="
echo "git add ."
echo "==================================================================================="
git add .

# Commit the version update
echo "==================================================================================="
echo "git commit -m \"Update version to $MASTER_BRANCH\""
echo "==================================================================================="
git commit -m "Update version to $MASTER_BRANCH"

# Create a tag for the commit
echo "==================================================================================="
echo "git tag -a \"v$RELEASE_VERSION\" -m \"Tagging version $MASTER_BRANCH\""
echo "==================================================================================="
git tag --delete "v$RELEASE_VERSION"

git tag -a "v$RELEASE_VERSION" -m "Tagging version $MASTER_BRANCH"

# git remote add origin https://axafrance.visualstudio.com/DefaultCollection/Automateam/_git/kafka-spark

# Push the changes to the remote repository
echo "==================================================================================="
echo "git push -u origin $MASTER_BRANCH"
echo "==================================================================================="
git push -u origin $MASTER_BRANCH

# Push the tag to the remote repository
echo "==================================================================================="
echo "git push -u origin tag \"v$RELEASE_VERSION\""
echo "==================================================================================="
git push -u origin tag "v$RELEASE_VERSION"

echo "==================================================================================="
echo "Merged $RELEASE_BRANCH into $MASTER_BRANCH and updated version to $RELEASE_BRANCH."


#!/bin/bash

# Variables
RELEASE_VERSION=$1
SNAPSHOT_VERSION=$2
RELEASE_BRANCH="release/$RELEASE_VERSION"
DEVELOP_BRANCH="develop"

# Function to update the version in pom.xml
update_version() {
  mvn versions:set -DnewVersion=$1
  mvn versions:update-child-modules
  mvn versions:commit
}

# save work
git stash push -u -m "Stash changes before creating release branch"

# Checkout release branch
git checkout -f -B $RELEASE_BRANCH origin/$RELEASE_BRANCH

# Pull the latest changes
git pull origin $RELEASE_BRANCH

# save work
git stash push -u -m "Stash changes before creating develop branch"

# Checkout develop branch
git checkout -f -B $DEVELOP_BRANCH origin/$DEVELOP_BRANCH

# Pull the latest changes
git pull origin $DEVELOP_BRANCH

# Merge the release branch into develop
git merge --ff $RELEASE_BRANCH

# Update the version in pom.xml
update_version $SNAPSHOT_VERSION

# Add the changes to the index
git add .

# Commit the version update
git commit -m "Update version to $DEVELOP_BRANCH"

# Create a tag for the commit
#git tag -a "v$RELEASE_VERSION" -m "Tagging version $MAIN_BRANCH"

# Push the changes to the remote repository
git push origin $DEVELOP_BRANCH

# Push the tag to the remote repository
#git push origin tag "v$RELEASE_VERSION"

echo "Merged $RELEASE_BRANCH into $DEVELOP_BRANCH and updated version to $SNAPSHOT_VERSION."


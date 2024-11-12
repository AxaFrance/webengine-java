#!/bin/bash

# Variables
RELEASE_VERSION=$1
RELEASE_BRANCH="release/$RELEASE_VERSION"

# save work
git stash push -u -m "Stash changes before creating develop branch"

# Checkout develop branch
git checkout -f -B develop origin/develop

# Pull the latest changes
git pull origin develop

# save work
git stash push -u -m "Stash changes before creating release branch"

# Create a new release branch
git checkout -f -B $RELEASE_BRANCH

# Push the new release branch to the remote repository
git push origin $RELEASE_BRANCH

echo "Release branch $RELEASE_BRANCH created and pushed to remote."
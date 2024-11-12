#!/bin/bash

# Variables
RELEASE_VERSION=$1
RELEASE_BRANCH="release/$RELEASE_VERSION"
PREPROD_BRANCH="preprod/$RELEASE_VERSION"

# save work
git stash push -u -m "Stash changes before creating release branch"

# Checkout release branch
git checkout -f -B $RELEASE_BRANCH origin/$RELEASE_BRANCH

# Pull the latest changes
git pull origin $RELEASE_BRANCH

# save work
git stash push -u -m "Stash changes before creating preprod branch"

# Checkout preprod branch
git checkout -f -B $PREPROD_BRANCH

# Merge the release into preprod
git merge --ff $RELEASE_BRANCH

# Push the changes to the remote repository
git push origin $PREPROD_BRANCH

echo "Merged $RELEASE_BRANCH into $PREPROD_BRANCH."
#!/usr/bin/env bash
#
# Zip up package for remote deployment.
#
# Usage: package.sh [GIT_TAG_NAME]
#   The optional argument GIT_TAG_NAME is default to 'HEAD'.
#
# Env variable options:
#   VERSION Define explicit package version label. Default to first 7 chars of sha1 id of git commit.
#

SCRIPT_DIR=$(cd `dirname $0` && pwd)
PROJ_HOME=$SCRIPT_DIR/..

# Setup args
RELEASE_TAG='HEAD'
if [[ $# -ge 1 ]]; then
    RELEASE_TAG=$1
fi

# == Build it with release tag
pushd $PROJ_HOME

# Create release version props file
RELEASE_FILE=$PROJ_HOME/src/main/resources/adocblog/release.properties
COMMIT_ID=$(git rev-parse $RELEASE_TAG)
BUILD_DATE=$(date +%Y-%m-%dT%H:%M:%S%z)
VERSION=${VERSION:=${COMMIT_ID:0:7}}

echo "commit-id=$COMMIT_ID
build-date=$BUILD_DATE
version=$VERSION" > $RELEASE_FILE
echo "Release file created: $RELEASE_FILE"
cat $RELEASE_FILE

# Maven artifact package
mvn clean package -DskipTests -Ppackage-no-app-props || exit 1

# Clean up
rm -vf $RELEASE_FILE
popd

# Release package it

# == Prepare package dir
PACKAGE_DIR=$PROJ_HOME/target/release-package/adocblog-$VERSION
if [[ -d $PACKAGE_DIR ]]; then
	echo "Removing existing release package $PACKAGE_DIR"
	rm -rf $PACKAGE_DIR
fi
echo "Creating new release package $PACKAGE_DIR"
mkdir -p $PACKAGE_DIR

# Copy config files
mkdir -p $PACKAGE_DIR/config
cp -rvf $PROJ_HOME/src/main/resources/adocblog/app*.properties $PACKAGE_DIR/config

# Copy war
FINAL_PKG_FILE=$PACKAGE_DIR/adocblog-$VERSION.war
cp -vf $PROJ_HOME/target/adocblog.war $FINAL_PKG_FILE

# Zip it up
zip -r $PACKAGE_DIR.zip $PACKAGE_DIR

# Done
echo "Package $PACKAGE_DIR.zip created"

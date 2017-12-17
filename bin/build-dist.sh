#!/usr/bin/env bash
#
# Zip up a package for remote deployment. This script will perform the following main tasks:
#  - Generate a release.properties as part of the packaged application jar
#  - Generate a skinny war
#  - Generate all dependency jars in lib directory
#  - Move all app*.properties files into config directory
#
# Usage: build-dist.sh [GIT_TAG_NAME]
#     GIT_TAG_NAME - an optional argument to specify Git tag name. Default to current branch 'HEAD'.
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
mvn clean package -DskipTests -Ppackage-zip || exit 1

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

# Copy root files
cp -vf $PROJ_HOME/readme.adoc $PACKAGE_DIR
cp -vf $PROJ_HOME/LICENSE $PACKAGE_DIR

# Copy bin files
mkdir -p $PACKAGE_DIR/bin
cp -vf $PROJ_HOME/bin/setenv.sh $PACKAGE_DIR/bin
cp -vf $PROJ_HOME/bin/version.sh $PACKAGE_DIR/bin
cp -vf $PROJ_HOME/bin/create-user.sh $PACKAGE_DIR/bin
cp -vf $PROJ_HOME/bin/server.sh $PACKAGE_DIR/bin
cp -vf $PROJ_HOME/bin/init-db.sh $PACKAGE_DIR/bin

# Copy config files
mkdir -p $PACKAGE_DIR/config/adocblog
#cp -rvf $PROJ_HOME/src/main/resources/adocblog/app*.properties $PACKAGE_DIR/config/adocblog

# Copy war
cp -vf $PROJ_HOME/target/adocblog.war $PACKAGE_DIR/adocblog.war

# Copy extra dependency
mkdir -p $PACKAGE_DIR/lib
cp -vf $PROJ_HOME/target/dependency/* $PACKAGE_DIR/lib

# This is only needed to support skinny-war packaging
#cp -vf $PROJ_HOME/target/adocblog/WEB-INF/lib/* $PACKAGE_DIR/lib

# Copy app jar itself for utility purpose
#cp -vf $PROJ_HOME/target/adocblog/WEB-INF/adocblog*.jar $PACKAGE_DIR/lib

# Zip it up
zip -r $PACKAGE_DIR.zip $PACKAGE_DIR

# Done
echo "Package $PACKAGE_DIR.zip created"

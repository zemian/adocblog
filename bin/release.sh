#!/usr/bin/env bash
#
# Perform Local Git with Maven release and then build a distribution package.
#
# This helps tag git repository and increment the pom files version value. The git repository must be in
# clean state before this script is run.
#
# NOTE: For local git release, you must provide a connectionUrl during git checkout here in this script since
# the one from pom.xml will not work!
#
# Usage:
#   build-release.sh
#

# OS specific support.	$var _must_ be set to either true or false.
IS_CYGWIN=false
case "`uname`" in
	CYGWIN*) IS_CYGWIN=true;;
esac

SCRIPT_DIR=$(cd `dirname $0` && pwd)
PROJ_HOME=$SCRIPT_DIR/..
if [[ $IS_CYGWIN ]]; then
    PROJ_HOME=`cygpath -wm $PROJ_HOME`
fi

echo "Using project directory: $PROJ_HOME"
mvn release:prepare -DpushChanges=false -Darguments="-DskipTests" || exit 1
mvn release:perform -Dgoals=install -Darguments="-DskipTests" -DconnectionUrl=scm:git:file://$PROJ_HOME || exit 1
#git push && git push --tags || exit 1

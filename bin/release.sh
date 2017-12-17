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
#   release.sh
#

# Ensure script exit upon any error from any commands
set -e

# OS specific support.	$var _must_ be set to either true or false.
IS_CYGWIN=false
case "`uname`" in
	CYGWIN*) IS_CYGWIN=true;;
esac

SCRIPT_DIR=$(cd `dirname $0` && pwd)
PROJ_HOME=$SCRIPT_DIR/..
if [[ "$IS_CYGWIN" = true ]]; then
    PROJ_HOME=`cygpath -wm $PROJ_HOME`
fi

echo "Using project directory: $PROJ_HOME"
mvn release:prepare -DpushChanges=false -Darguments="-DskipTests"
mvn release:perform -Dgoals=install -Darguments="-DskipTests" -DconnectionUrl=scm:git:file://$PROJ_HOME
#git push && git push --tags

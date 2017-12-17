#!/usr/bin/env bash
#
# Start a standalone servlet server using webapp-runner.jar
#   https://github.com/jsimone/webapp-runner
#
# Usage:
#  server.sh --port 8080
#

# OS specific support.	$var _must_ be set to either true or false.
IS_CYGWIN=false
case "`uname`" in
	CYGWIN*) IS_CYGWIN=true;;
esac

SCRIPT_DIR=$(cd `dirname $0` && pwd)
PROJ_HOME=$SCRIPT_DIR/..
CP_SEP=':'
if [[ "$IS_CYGWIN" = true ]]; then
    PROJ_HOME=`cygpath -wm $PROJ_HOME`
    CP_SEP=';'
fi

JAVA_OPTS=${JAVA_OPTS:=}

echo "Starting tomcat server in $PROJ_HOME"
#echo "Using JAVA_OPTS=$JAVA_OPTS"
java $JAVA_OPTS -cp "$PROJ_HOME/config${CP_SEP}$PROJ_HOME/lib/*" -jar $PROJ_HOME/lib/webapp-runner.jar $PROJ_HOME/adocblog.war

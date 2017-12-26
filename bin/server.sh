#!/usr/bin/env bash
#
# Start a standalone servlet server using webapp-runner.jar
#   https://github.com/jsimone/webapp-runner
#
# Usage:
#  server.sh --port 8080
#

SCRIPT_DIR=$(cd `dirname $0` && pwd)
source $SCRIPT_DIR/setenv.sh
java $JAVA_OPTS -cp $JAVA_CP -jar $PROJ_HOME/lib/webapp-runner.jar $PROJ_HOME/adocblog

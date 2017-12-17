#!/usr/bin/env bash
#
# Utility to print the current release info.
#
# Usage:
#   version.sh
#

SCRIPT_DIR=$(cd `dirname $0` && pwd)
source $SCRIPT_DIR/setenv.sh
java $JAVA_OPTS -cp $JAVA_CP com.zemian.adocblog.app.PrintReleaseProps "$@"

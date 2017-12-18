#!/usr/bin/env bash
#
# Utility to create user for application.
#
# Usage:
#   create-user.sh [--adminUser=true] [--fullName=NAME] <username> <password>
#

SCRIPT_DIR=$(cd `dirname $0` && pwd)
source $SCRIPT_DIR/setenv.sh
java $JAVA_OPTS -cp $JAVA_CP com.zemian.adocblog.app.UserTool "$@"

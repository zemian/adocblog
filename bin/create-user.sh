#!/usr/bin/env bash
#
# Utility to create user for application.
#
# Usage:
#   create-user.sh [--adminUser=true] [--fullName=NAME] <username> <password>
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
CP_SEP=':'
if [[ "$IS_CYGWIN" = true ]]; then
    PROJ_HOME=`cygpath -wm $PROJ_HOME`
    CP_SEP=';'
fi

# If running in local dev
if [[ -d $PROJ_HOME/target/classes ]]; then
    CP="$PROJ_HOME/target/classes${CP_SEP}$PROJ_HOME/target/dependency/*"
else
    # Ensure we unpack the war for jar dependencies before running utility
    WAR_DIR=$PROJ_HOME/target/tomcat.8080/webapps/expanded
    LIB_DIR=$WAR_DIR/WEB-INF/lib
    if [[ ! -d $WAR_DIR ]]; then
        mkdir -p $WAR_DIR
        pushd $WAR_DIR
        jar xvf $PROJ_HOME/adocblog.war
        popd
    fi
    CP="$PROJ_HOME/config${CP_SEP}$LIB_DIR/*"
fi

JAVA_OPTS=${JAVA_OPTS:=}

echo "Creating user in $PROJ_HOME"
#echo "Using JAVA_OPTS=$JAVA_OPTS"
java $JAVA_OPTS -cp $CP com.zemian.adocblog.app.CreateUser "$@"

#!/usr/bin/env bash
#
# Utility to create user for application.
#
# Usage:
#   JAVA_OPTS="-DadminUser=true -DfirstName=First -DlastName=Last" create-user.sh <username> <password>
#

# OS specific support.	$var _must_ be set to either true or false.
IS_CYGWIN=false
case "`uname`" in
	CYGWIN*) IS_CYGWIN=true;;
esac

SCRIPT_DIR=$(cd `dirname $0` && pwd)
PROJ_HOME=$SCRIPT_DIR/..
CP_SEP=':'
if [[ $IS_CYGWIN ]]; then
    PROJ_HOME=`cygpath -wm $PROJ_HOME`
    CP_SEP=';'
fi

# Ensure we unpack the war for jar dependencies before running utility
WAR_DIR=$PROJ_HOME/target/tomcat.8080/webapps/expanded
LIB_DIR=$WAR_DIR/WEB-INF/lib
if [[ ! -d $WAR_DIR ]]; then
    mkdir -p $WAR_DIR
    pushd $WAR_DIR
    jar xvf $PROJ_HOME/adocblog.war
    popd
fi

JAVA_OPTS=${JAVA_OPTS:=}

echo "Creating user in $PROJ_HOME"
echo "Using JAVA_OPTS=$JAVA_OPTS"
java $JAVA_OPTS -cp "$PROJ_HOME/config${CP_SEP}$LIB_DIR/*" com.zemian.adocblog.app.CreateUser "$@"

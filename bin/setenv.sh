#!/usr/bin/env bash
#
# A common env to support other scripts.
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

# If running in local dev
if [[ -d $PROJ_HOME/target/classes ]]; then
    if [[ ! -d $PROJ_HOME/target/dependency ]]; then
        mvn compile dependency:copy-dependencies
    fi
    JAVA_CP="$PROJ_HOME/target/classes${CP_SEP}$PROJ_HOME/target/dependency/*"
else
    # Ensure we unpack the war for jar dependencies before running utility
    LIB_DIR=$PROJ_HOME/adocblog/WEB-INF/lib
    JAVA_CP="$PROJ_HOME/config${CP_SEP}$LIB_DIR/*"
fi

JAVA_OPTS=${JAVA_OPTS:=}

export IS_CYGWIN PROJ_HOME CP_SEP JAVA_CP JAVA_OPTS

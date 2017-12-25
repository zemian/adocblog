#!/usr/bin/env bash
#
# Utility to create user for application.
#
# Usage:
#   doc-tool.sh [--options] <command>
#   doc-tool.sh --help
#

SCRIPT_DIR=$(cd `dirname $0` && pwd)
source $SCRIPT_DIR/setenv.sh
java $JAVA_OPTS -cp $JAVA_CP com.zemian.adocblog.app.DocTool "$@"

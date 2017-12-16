#!/usr/bin/env bash
#
# Setup and initialize a new PostgreSQL DB named `adocblog` and a DB userid=`adocblog`.
#

# Create a new DB user and a database
SCRIPT_DIR=$(cd `dirname $0` && pwd)
psql -U postgres -f $SCRIPT_DIR/../db/create-db.sql

# Create schema
psql -U adocblog -f $SCRIPT_DIR/../db/create-schema.sql

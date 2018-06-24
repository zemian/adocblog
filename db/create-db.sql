-- We should execute this script with 'superuser'
-- This script prepare a DB user 'adocblog' and a empty database for app use.
CREATE USER adocblog;
CREATE DATABASE adocblog_dev WITH OWNER = adocblog;

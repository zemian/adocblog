-- SQL and Utilities to check PostGres DB and App Data

-- Get all tables and their row counts
-- Note that these counts are not 100% accurate!
-- See https://stackoverflow.com/questions/2596670/how-do-you-find-the-row-count-for-all-your-tables-in-postgres
SELECT schemaname as schema, relname as table, n_live_tup as row_count
  FROM pg_stat_user_tables
  ORDER BY n_live_tup DESC;
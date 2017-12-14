-- == App configuration settings table

-- type: STRING, INTEGER, DOUBLE, LIST
CREATE TABLE settings (
  setting_id SERIAL NOT NULL PRIMARY KEY,
  category VARCHAR(50) NOT NULL,
  name VARCHAR(50) NOT NULL,
  value VARCHAR(5000) NOT NULL,
  type VARCHAR(50) NOT NULL DEFAULT 'STRING',
  description VARCHAR(1000) NULL
);

-- Sample of app.properties override
-- INSERT INTO settings (category, name, value) VALUES ('APP_CONFIG_DEV', 'app.web.name', 'Zemian''s Blog');
-- INSERT INTO settings (category, name, value) VALUES ('APP_CONFIG_DEV', 'app.web.title', 'Zemian''s Blog');
-- INSERT INTO settings (category, name, value) VALUES ('APP_CONFIG_DEV', 'app.web.blogTitle', 'Writings on Java and other programming notes.');

CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(500) NOT NULL,
  full_name VARCHAR(500) NOT NULL,
  is_admin BOOLEAN NOT NULL DEFAULT FALSE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- A dummy user used to record anonymous user activity (eg: comment posting)
-- INSERT INTO users (username, password, full_name) VALUES('anonymous', '', 'anonymous user');

CREATE TABLE contents (
  content_id SERIAL NOT NULL PRIMARY KEY,
  title VARCHAR(2000) NOT NULL,
  content_text TEXT NOT NULL,
  version SMALLINT NOT NULL,
  reason_for_edit VARCHAR(1000) NULL,
  format VARCHAR(500) NOT NULL DEFAULT 'ADOC',
  created_user VARCHAR(50) NOT NULL REFERENCES users(username),
  created_dt TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE docs (
  doc_id SERIAL NOT NULL PRIMARY KEY,
  path VARCHAR(2000) NOT NULL UNIQUE,
  type VARCHAR(500) NOT NULL DEFAULT 'PAGE',
  latest_content_id INT NOT NULL REFERENCES contents(content_id),
  published_content_id INT NULL REFERENCES contents(content_id),
  published_user VARCHAR(50) NULL REFERENCES users(username),
  published_dt TIMESTAMPTZ NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  reason_for_delete VARCHAR(1000) NULL
);

-- This is a link table
-- One doc contains one or more versioned content
CREATE TABLE doc_contents (
  doc_id INT NOT NULL REFERENCES docs(doc_id),
  content_id INT NOT NULL REFERENCES contents(content_id)
);

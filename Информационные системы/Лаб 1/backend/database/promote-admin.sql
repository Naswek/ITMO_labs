-- Run with: psql ... -v login=YOUR_LOGIN -f backend/database/promote-admin.sql
-- Only a database operator may assign this role; a public registration stays a regular user.
\set ON_ERROR_STOP on
UPDATE app_user
SET is_admin = true
WHERE login = :'login'
RETURNING id, login, is_admin;

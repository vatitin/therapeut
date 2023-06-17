-- https://www.postgresql.org/docs/current/sql-createrole.html
CREATE ROLE therapeut LOGIN PASSWORD 'p';

-- https://www.postgresql.org/docs/current/sql-createdatabase.html
CREATE DATABASE therapeut;

-- https://www.postgresql.org/docs/current/sql-grant.html
GRANT ALL ON DATABASE therapeut TO therapeut;

-- https://www.postgresql.org/docs/10/sql-createtablespace.html
CREATE TABLESPACE therapeutspace OWNER therapeut LOCATION '/var/lib/postgresql/tablespace/therapeut';

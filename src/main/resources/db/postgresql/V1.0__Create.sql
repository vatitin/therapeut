-- Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <https://www.gnu.org/licenses/>.

-- docker compose exec postgres bash
-- psql --dbname=therapeut --username=therapeut --file=/sql/V1.0__Create.sql

-- https://www.postgresql.org/docs/current/sql-createtable.html
-- https://www.postgresql.org/docs/current/datatype.html
-- BEACHTE: user ist ein Schluesselwort
/*
CREATE TABLE IF NOT EXISTS login (
             -- https://www.postgresql.org/docs/current/datatype-uuid.html
             -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-PRIMARY-KEYS
             -- impliziter Index fuer Primary Key
    id       uuid PRIMARY KEY USING INDEX TABLESPACE therapeutspace,
    username varchar(20) NOT NULL UNIQUE USING INDEX TABLESPACE therapeutspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE therapeutspace;
 */

-- https://www.postgresql.org/docs/current/sql-createtype.html
-- https://www.postgresql.org/docs/current/datatype-enum.html
-- CREATE TYPE geschlecht AS ENUM ('MAENNLICH', 'WEIBLICH', 'DIVERS');

CREATE TABLE IF NOT EXISTS therapeut (
    id            uuid PRIMARY KEY USING INDEX TABLESPACE therapeutspace,
                  -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT
    nachname      varchar(40) NOT NULL,
    vorname      varchar(40) NOT NULL,

  -- impliziter Index als B-Baum durch UNIQUE
                  -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-UNIQUE-CONSTRAINTS
    email         varchar(40) NOT NULL UNIQUE USING INDEX TABLESPACE therapeutspace,
                  -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-CHECK-CONSTRAINTS
                  -- https://www.postgresql.org/docs/current/datatype-datetime.html
  geschlecht    varchar(9) CHECK (geschlecht ~ 'MAENNLICH|WEIBLICH|DIVERS'),
  geburtsdatum  date CHECK (geburtsdatum < current_date),
    taetigkeitsbereiche    varchar(32),
                  -- https://www.postgresql.org/docs/current/datatype-datetime.html
  erzeugt       timestamp NOT NULL,
    aktualisiert  timestamp NOT NULL
) TABLESPACE therapeutspace;

-- default: btree
-- https://www.postgresql.org/docs/current/sql-createindex.html
CREATE INDEX IF NOT EXISTS therapeut_nachname_idx ON therapeut(nachname) TABLESPACE therapeutspace;

CREATE TABLE IF NOT EXISTS adresse (
    id        uuid PRIMARY KEY USING INDEX TABLESPACE therapeutspace,
    plz       char(5) NOT NULL CHECK (plz ~ '\d{5}'),
    ort       varchar(40) NOT NULL,
    -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-FK
    therapeut_id  uuid NOT NULL UNIQUE USING INDEX TABLESPACE therapeutspace REFERENCES therapeut
) TABLESPACE therapeutspace;
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse(plz) TABLESPACE therapeutspace;


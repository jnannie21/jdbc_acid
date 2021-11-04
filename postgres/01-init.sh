#!/bin/bash
set -e
psql --username admin --dbname postgres <<-EOSQL
  \connect postgres admin
  BEGIN;
    CREATE TABLE IF NOT EXISTS team (
      id SERIAL not NULL,
      color VARCHAR(255) NOT NULL UNIQUE, CHECK (color <> ''),
      points INTEGER NOT NULL, PRIMARY KEY (id)
    );

    CREATE TABLE IF NOT EXISTS student (
      id SERIAL NOT NULL,
      first_name VARCHAR(255) NOT NULL, CHECK (first_name <> ''),
      last_name VARCHAR(255) NOT NULL, CHECK (last_name <> ''),
      age INTEGER NOT NULL, CHECK (age > 0),
      points INTEGER NOT NULL,
      team_id INTEGER not NULL,
      PRIMARY KEY (id),
      CONSTRAINT fk_team FOREIGN KEY(team_id) REFERENCES team(id)
    );
  COMMIT;

  BEGIN;
    insert into team (color, points) values ('green', 10);
    insert into team (color, points) values ('red', 20);

    insert into student (first_name, last_name, age, points, team_id) values ('dima', 'dmitriev', 33, 10, 1);
    insert into student (first_name, last_name, age, points, team_id) values ('vasia', 'vasilev', 25, 15, 2);
  COMMIT;
EOSQL
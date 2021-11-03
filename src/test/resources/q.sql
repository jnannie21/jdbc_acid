CREATE TABLE team (id SERIAL not NULL, color VARCHAR(255) NOT NULL UNIQUE, CHECK (color <> ''), points INTEGER NOT NULL, PRIMARY KEY (id));

CREATE TABLE student (id SERIAL NOT NULL,
                    first_name VARCHAR(255) NOT NULL,
                    CHECK (first_name <> ''),
                    last_name VARCHAR(255) NOT NULL,
                    CHECK (last_name <> ''),
                    age INTEGER NOT NULL,
                    CHECK (age <> 0),
                    points INTEGER NOT NULL,
                    team_id INTEGER not NULL,
                    PRIMARY KEY (id),
                    CONSTRAINT fk_team
                    FOREIGN KEY(team_id)
                    REFERENCES team(id));

insert into team (id, color, points) values (1, 'green', 10);
insert into team (id, color, points) values (2, 'red', 15);
insert into student (id, first_name, last_name, age, points, team_id) values (1, 'dima', 'polushkin', 33, 21, 1);
insert into student (id, first_name, last_name, age, points, team_id) values (2, 'vasia', 'vasin', 44, 22, 1);
insert into student (id, first_name, last_name, age, points, team_id) values (3, 'kolia', 'kolin', 15, 20, 2);
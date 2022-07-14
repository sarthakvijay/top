DROP TABLE IF EXISTS TEAM;

CREATE TABLE TEAM (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  country VARCHAR(250) NOT NULL,
  marketvalue DOUBLE NOT NULL
);

DROP TABLE IF EXISTS PLAYER;

CREATE TABLE PLAYER (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  country VARCHAR(250) NOT NULL,
  team_id INT NOT NULL,
  positions VARCHAR(250) NOT NULL,
  marketvalue DOUBLE NOT NULL,
  age INT NOT NULL
);


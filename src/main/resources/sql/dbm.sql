-- SCRIPT INFORMATION --
-- Types: mysql mariadb
-- Version: 1
-- Upgrades: 0
-- SCRIPT INFORMATION --

START TRANSACTION;
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS gaia_states;

CREATE TABLE gaia_states (
  id              BIGINT      NOT NULL    AUTO_INCREMENT,
  x               INTEGER     NOT NULL,
  y               INTEGER     NOT NULL,
  z               INTEGER     NOT NULL,
  world_uuid      BINARY(16)  NOT NULL,
  plant_state     INTEGER     NOT NULL,
  transition_time TIMESTAMP   NOT NULL,
  planted_time    TIMESTAMP   NOT NULL,
  creator         BINARY(16)  NOT NULL,
  plant           VARCHAR(64) NOT NULL,
  growth_count    INTEGER     NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (x, y, z, world_uuid, plant_state, transition_time)
);

CREATE INDEX transition_index ON gaia_states (transition_time);

SET foreign_key_checks = 1;
COMMIT;
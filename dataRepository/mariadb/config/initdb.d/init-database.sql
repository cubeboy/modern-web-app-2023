CREATE SCHEMA IF NOT EXISTS task_agile DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE task_agile ;

-- -----------------------------------------------------
-- Table task_agile.users
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.users ;

CREATE TABLE IF NOT EXISTS task_agile.users (
  user_id INT(11) NOT NULL AUTO_INCREMENT,
  email_address VARCHAR(128) NOT NULL,
  username VARCHAR(64) NOT NULL,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (user_id),
  UNIQUE INDEX email_address_uidx (email_address ASC),
  UNIQUE INDEX username_uidx (username ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.team
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.team ;

CREATE TABLE IF NOT EXISTS task_agile.team (
  team_id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  user_id INT(11) NOT NULL,
  archived TINYINT(1) NOT NULL DEFAULT 0,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (team_id),
  INDEX fk_user_id_idx (user_id ASC),
  CONSTRAINT fk_team_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.board
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.board ;

CREATE TABLE IF NOT EXISTS task_agile.board (
  board_id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(256) NOT NULL,
  user_id INT(11) NOT NULL,
  team_id INT(11) NULL,
  archived TINYINT(1) NOT NULL DEFAULT 0,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (board_id),
  INDEX fk_team_id_idx (team_id ASC),
  INDEX fk_user_id_idx (user_id ASC),
  CONSTRAINT fk_board_team_team_id
    FOREIGN KEY (team_id)
    REFERENCES task_agile.team (team_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_board_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.board_member
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.board_member ;

CREATE TABLE IF NOT EXISTS task_agile.board_member (
  board_id INT(11) NOT NULL,
  user_id INT(11) NOT NULL,
  INDEX fk_board_id_idx (board_id ASC),
  INDEX fk_user_id_idx (user_id ASC),
  PRIMARY KEY (user_id, board_id),
  CONSTRAINT fk_board_member_board_board_id
    FOREIGN KEY (board_id)
    REFERENCES task_agile.board (board_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_board_member_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.card_list
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.card_list ;

CREATE TABLE IF NOT EXISTS task_agile.card_list (
  card_list_id INT(11) NOT NULL AUTO_INCREMENT,
  board_id INT(11) NOT NULL,
  user_id INT(11) NOT NULL,
  name VARCHAR(128) NOT NULL,
  position INT(11) NOT NULL,
  archived TINYINT(1) NOT NULL DEFAULT 0,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (card_list_id),
  INDEX fk_board_id_idx (board_id ASC),
  INDEX fk_user_id_idx (user_id ASC),
  CONSTRAINT fk_card_list_board_board_id
    FOREIGN KEY (board_id)
    REFERENCES task_agile.board (board_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_card_list_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.card
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.card ;

CREATE TABLE IF NOT EXISTS task_agile.card (
  card_id INT(11) NOT NULL AUTO_INCREMENT,
  card_list_id INT(11) NOT NULL,
  user_id INT(11) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  position INT(11) NOT NULL,
  archived TINYINT(1) NOT NULL,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (card_id),
  INDEX fk_card_list_id_idx (card_list_id ASC),
  INDEX fk_user_id_idx (user_id ASC),
  CONSTRAINT fk_card_card_list_card_list_id
    FOREIGN KEY (card_list_id)
    REFERENCES task_agile.card_list (card_list_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_card_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.assignment
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.assignment ;

CREATE TABLE IF NOT EXISTS task_agile.assignment (
  card_id INT(11) NOT NULL,
  user_id INT(11) NOT NULL,
  PRIMARY KEY (card_id, user_id),
  INDEX fk_user_id_idx (user_id ASC),
  CONSTRAINT fk_assignment_card_card_id
    FOREIGN KEY (card_id)
    REFERENCES task_agile.card (card_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_assignment_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.attachment
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.attachment ;

CREATE TABLE IF NOT EXISTS task_agile.attachment (
  attachment_id INT(11) NOT NULL AUTO_INCREMENT,
  card_id INT(11) NOT NULL,
  user_id INT(11) NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  file_path VARCHAR(255) NOT NULL,
  file_type INT(11) NOT NULL,
  archived TINYINT(1) NOT NULL DEFAULT 0,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (attachment_id),
  INDEX fk_card_id_idx (card_id ASC),
  INDEX fk_user_id_idx (user_id ASC),
  CONSTRAINT fk_attachment_card_card_id
    FOREIGN KEY (card_id)
    REFERENCES task_agile.card (card_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_attachment_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table task_agile.activity
-- -----------------------------------------------------
DROP TABLE IF EXISTS task_agile.activity ;

CREATE TABLE IF NOT EXISTS task_agile.activity (
  activity_id INT(11) NOT NULL AUTO_INCREMENT,
  user_id INT(11) NOT NULL,
  card_id INT(11) NULL,
  board_id INT(11) NOT NULL,
  type TINYINT(1) NOT NULL DEFAULT 0,
  detail JSON NOT NULL,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (activity_id),
  INDEX fk_user_id_idx (user_id ASC),
  INDEX fk_board_id_idx (board_id ASC),
  INDEX fk_card_id_idx (card_id ASC),
  CONSTRAINT fk_activity_user_user_id
    FOREIGN KEY (user_id)
    REFERENCES task_agile.users (user_id)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT fk_activity_board_board_id
    FOREIGN KEY (board_id)
    REFERENCES task_agile.board (board_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT fk_activity_card_card_id
    FOREIGN KEY (card_id)
    REFERENCES task_agile.card (card_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

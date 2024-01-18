CREATE TABLE users (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  email_address VARCHAR(128) NOT NULL,
  username VARCHAR(64) NOT NULL,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_date DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (user_id)
);

insert into users(username, email_address, password, first_name, last_name) values
('username1', 'username1@taskagile.com', 'P@ssword1', '', ''),
('username2', 'username2@taskagile.com', 'P@ssword2', '', ''),
('username3', 'username3@taskagile.com', 'P@ssword3', '', '')
;

drop table if exists user;
create table user
(
  username       VARCHAR(200),
  role           VARCHAR(100),
  name           VARCHAR(100),
  surname        VARCHAR(100),
  PRIMARY KEY (username)
);

insert into user values('user.test@gmail.com','admin', 'User', 'Test');

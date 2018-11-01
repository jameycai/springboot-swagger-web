create table security_rest_user
(
  user_id             NUMERIC(20) not null,
  account             varchar(32) not null,
  user_name           varchar(50) not null,
  pass                varchar(100) not null,
  dept_id             varchar(32),
  business            varchar(100),
  user_status         NUMERIC(8) default 0,
  primary key (ACCOUNT)
);
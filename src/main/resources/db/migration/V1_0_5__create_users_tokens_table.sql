create table users_tokens
(
    id      int          not null auto_increment,
    token   varchar(500) not null,
    uid     varchar(250) not null,
    user_id int          not null,
    primary key (id),
    unique key (token)
);
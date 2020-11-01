create table users
(
    id       int                    not null auto_increment,
    email    varchar(50)            not null,
    password varchar(500)           not null,
    enabled  boolean                not null,
    role     enum ('USER', 'ADMIN') not null,
    primary key (id),
    unique key (email)
);
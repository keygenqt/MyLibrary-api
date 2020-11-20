create table users
(
    id       int                    not null auto_increment,
    email    varchar(250)           not null,
    nickname varchar(250)           not null,
    image    varchar(500)           not null,
    password varchar(500)           not null,
    enabled  boolean                not null,
    role     enum ('USER', 'ADMIN') not null,
    avatar   enum (
                    'avatar_0',
                    'avatar_1',
                    'avatar_2',
                    'avatar_3',
                    'avatar_4',
                    'avatar_5',
                    'avatar_6',
                    'avatar_7',
                    'avatar_8',
                    'avatar_9',
                    'avatar_10',
                    'avatar_11',
                    'avatar_12',
                    'avatar_13',
                    'avatar_14') not null default 'avatar_0',
    primary key (id),
    unique key (email)
);
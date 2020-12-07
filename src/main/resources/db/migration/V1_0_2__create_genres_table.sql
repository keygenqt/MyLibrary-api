create table genres
(
    id              int                    not null auto_increment,
    title           varchar(250)           not null,
    description     TEXT                   not null,
    created_at      TIMESTAMP              not null default now(),
    updated_at      TIMESTAMP              not null default now(),
    primary key (id)
);
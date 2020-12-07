create table books
(
    id              int                    not null auto_increment,
    user_id         int                    not null,
    genre_id        int                    not null,
    image           varchar(250),
    title           varchar(250),
    author          varchar(250),
    publisher       varchar(250),
    isbn            varchar(250),
    year            int,
    number_of_pages int,
    description     TEXT,
    cover_type      enum ('Soft', 'Solid', 'Other') not null,
    sale            boolean                not null default 0,
    enabled         boolean                not null default 1,
    created_at      TIMESTAMP              not null default now(),
    updated_at      TIMESTAMP              not null default now(),
    primary key (id)
);
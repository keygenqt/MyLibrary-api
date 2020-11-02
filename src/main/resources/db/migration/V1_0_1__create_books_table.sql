create table books
(
    id              int                    not null auto_increment,
    user_id         int                    not null,
    genre_id        int                    not null,
    title           varchar(250)           not null,
    author          varchar(250)           not null,
    description     TEXT                   not null,
    publisher       varchar(250)           not null,
    year            int                    not null,
    isbn            varchar(250)           not null,
    number_of_pages int                    not null,
    cover_type      enum ('soft', 'solid', 'unknown') not null,
    image           varchar(250)           not null,
    primary key (id)
);
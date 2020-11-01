create table books
(
    id              int                    not null auto_increment,
    name            varchar(250)           not null,
    author          varchar(250)           not null,
    publisher       varchar(250)           not null,
    year            int                    not null,
    isbn            varchar(250)           not null,
    number_of_pages int                    not null,
    cover_type      enum ('soft', 'solid') not null,
    cover           varchar(250)           not null,
    primary key (id)
);
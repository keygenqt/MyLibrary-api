create table notifications
(
    id           int                                       not null auto_increment,
    user_id      int                                       not null,
    channel_id   varchar(250)                              not null default 'channel_server',
    title        varchar(250)                              not null default 'MyLibrary',
    uri          varchar(500),
    body         TEXT                                      not null,
    status       enum ('open', 'done', 'pending', 'error') not null,
    created_at   TIMESTAMP                                 not null default now(),
    primary key (id)
);
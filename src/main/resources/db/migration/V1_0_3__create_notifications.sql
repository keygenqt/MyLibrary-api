create table notifications
(
    id              int                                         not null auto_increment,
    user_id         int,
    notification    TEXT                                        not null,
    status          enum ('open', 'done', 'pending', 'error')   not null,
    created_at      TIMESTAMP                                   not null default now(),
    primary key (id)
);
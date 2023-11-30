create table if not exists wlg_2.accounts
(
    id       int                                         not null auto_increment,
    username varchar(36)                                 not null,
    pseudo   varchar(36)                                 null default null,
    password varchar(100)                                null default null,
    question varchar(64)                                 null default null,
    answer   varchar(256)                                null default null,
    roles    enum ('Player','Moderator','Administrator') null default null,
    primary key (id),
    unique index id (id),
    unique index username (username)
);
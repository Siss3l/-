create table if not exists wlg_2.characters
(
    id         int         not null auto_increment,
    account_id int         null default null,
    level      int         null default null,
    name       varchar(36) not null,
    gender     int         null default null,
    colors     text        null default null,
    gfx_id     int         null default null,
    hat        int         null default null,
    `date`     varchar(50) null default null,
    primary key (id),
    unique index id (id),
    index FK_characters_accounts (account_id),
    constraint FK_characters_accounts foreign key (account_id) references accounts (id)
);
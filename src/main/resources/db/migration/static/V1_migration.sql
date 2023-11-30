create table if not exists wlg_2.flyway_schema_history
(
    installed_rank int                                 not null,
    version        varchar(50),
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         text                                not null,
    checksum       int,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default current_timestamp not null,
    execution_time int                                 not null,
    name           varchar(300) generated always as (concat('V', replace(version, '.', '_'), '__', description, '.',
                                                            lower(type))) virtual,
    success        bool                                not null,
    primary key (installed_rank),
    constraint installed_rank unique (installed_rank),
    index idx (success)
) collate utf8mb4_general_ci;
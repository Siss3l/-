create table if not exists wlg_2.subareas
(
    id          int primary key,
    area_id     int,
    name        varchar(200),
    conquerable bool null default null,
    alignment   bool null default null
);

create table if not exists wlg_2.maps
(
    id         int auto_increment,
    `date`     varchar(50) null default null,
    h          int,
    w          int,
    x          int,
    y          int,
    subarea_id int,
    indoor     bool        null default null,
    `key`      text        null default null,
    mapdata    text,
    primary key (id),
    unique index id (id),
    constraint `date` unique (date),
    constraint FK_maps_subareas foreign key (subarea_id) references subareas (id)
);
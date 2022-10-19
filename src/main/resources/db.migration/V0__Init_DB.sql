create table hibernate_sequence (
    next_val bigint
);
insert into hibernate_sequence(next_val) values (1);

create table note (
    id integer not null,
    tag varchar(255),
    text varchar(2048) not null,
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table user_role
(
    user_id bigint not null,
    roles varchar(255)
) engine=InnoDB;

create table usr (
    id bigint not null,
    activation_code varchar(255),
    active bit not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
) engine=InnoDB;

alter table note
    add constraint note_user_fk
    foreign key (user_id) references usr(id);

alter table user_role
    add constraint role_user_fk
    foreign key (user_id) references usr(id);
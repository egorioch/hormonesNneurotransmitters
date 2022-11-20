create table note_likes (
    note_id bigint not null,
    user_id bigint not null,
    primary key (note_id, user_id)
);

alter table note_likes
    add constraint note_id_fk
    foreign key (note_id) references note (id);

alter table note_likes
    add constraint user_id_fk
    foreign key (user_id) references usr (id);
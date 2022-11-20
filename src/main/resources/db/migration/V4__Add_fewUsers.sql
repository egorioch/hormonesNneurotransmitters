insert into usr(id, username, password, active) values
    (2, 'bebra', 123, true),
    (3, 'beregNevi', 123, true);

insert into user_role(user_id, roles) values
    (2, 'USER'),
    (3, 'USER');

update hibernate_sequence set next_val='4';


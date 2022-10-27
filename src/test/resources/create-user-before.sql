#delete from user_role;
#delete from note;
#delete from user_subscriptions;
#delete from usr;

insert into usr(id, active, username, password) values
    (1, true, '123', 'admin'),
    (2, true, '123', 'shlep');


insert into user_role(user_id, roles) values
    (1, 'USER'), (1, 'ADMIN'),
    (2, 'USER');


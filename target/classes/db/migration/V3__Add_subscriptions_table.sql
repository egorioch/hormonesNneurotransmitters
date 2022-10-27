
create table user_subscriptions (
    subscriber_id bigint not null,
    channel_id bigint not null,
    primary key (channel_id, subscriber_id)
                                ) engine=InnoDB;

alter table user_subscriptions
    add constraint channel_id_fk
    foreign key (channel_id) references usr (id);

alter table user_subscriptions
    add constraint subscriber_id_fk
    foreign key (subscriber_id) references usr (id);
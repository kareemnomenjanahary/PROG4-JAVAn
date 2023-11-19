create table if not exists "subscribe"(
    id                varchar
            constraint subscribe_pk primary key default uuid_generate_v4(),
    subscriber_name varchar not null
);
create index if not exists subscriber_name_index on "subscribe" (subscriber_name);
do
$$
    begin
        if not exists(select from pg_type where typname = 'sex') then
            create type sex as enum ('M', 'F');
        end if;
    end
$$;
create table if not exists "author"
(
    id                varchar
        constraint author_pk primary key default uuid_generate_v4(),
    first_name        varchar                  not null,
    last_name         varchar                  not null,
    sex               sex                      not null,
    birth_date        date                     not null
);
create index if not exists author_last_name_index on "author" (last_name);

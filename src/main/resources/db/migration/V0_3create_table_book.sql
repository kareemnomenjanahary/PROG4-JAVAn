do
$$
    begin
        if not exists(select from pg_type where typname = 'topic') then
            create type user_status as enum ('ROMANCE','COMEDY' , 'OTHER' );
        end if;
    end
$$;
create table if not exists "book"
(
    id                varchar
        constraint book_pk primary key default uuid_generate_v4(),
    book_name        varchar                  not null,
    page_number        varchar                  not null,
    sex               sex                      not null,
    birth_date        date                     not null
);
create index if not exists book_name_index on "book" (book_name);

CREATE TABLE public.demo_user (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    city varchar(255),
    state varchar(255),
    address varchar(255),
    zip varchar(255),
    avatar varchar(255),
    favorites varchar(255)[],
    created_dt timestamptz,
    updated_dt timestamptz,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.demo_user OWNER TO admin;

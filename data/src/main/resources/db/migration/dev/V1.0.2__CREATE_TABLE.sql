CREATE TABLE public.user_profile (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id bigint,
    social_id bigint,
    security_id bigint,
    user_settings_id bigint,
    first_name varchar(255),
    last_name varchar(255),
    middle_name varchar(255),
    title varchar(255),
    gender varchar(8),
    email varchar(255),
    is_new_user boolean,
    avatar varchar(255),
    home_phone varchar(255),
    mobile_phone varchar(255),
    date_of_birth date,
    created_dt timestamptz,
    updated_dt timestamptz,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.user_profile OWNER TO learnet;

CREATE TABLE public.address (
      id BIGSERIAL PRIMARY KEY NOT NULL,
      country varchar(255),
      state varchar(255),
      city varchar(255),
      postal_code varchar(255),
      address_1 varchar(255),
      address_2 varchar(255),
      user_profile_id bigint,
      created_dt timestamptz,
      updated_dt timestamptz,
      _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.address OWNER TO learnet;

CREATE TABLE public.social (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    facebook varchar(255),
    twitter varchar(255),
    linkedin varchar(255),
    github varchar(255),
    youtube varchar(255),
    instagram varchar(255),
    snapchat varchar(255),
    whatsapp varchar(255),
    website varchar(255),
    other varchar(255),
    created_dt timestamptz,
    updated_dt timestamptz,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.social OWNER TO learnet;


CREATE TABLE public.security (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     roles varchar(255)[],
     created_dt timestamptz,
     updated_dt timestamptz,
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.security OWNER TO learnet;

CREATE TABLE public.user_settings (
      id BIGSERIAL PRIMARY KEY NOT NULL,
      timezone varchar(255),
      language varchar(255),
      created_dt timestamptz,
      updated_dt timestamptz,
      _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.user_settings OWNER TO learnet;

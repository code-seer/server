--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner:
--

-- COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';

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

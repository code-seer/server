--
-- Name: ltree; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS ltree WITH SCHEMA public;


--
-- Name: EXTENSION ltree; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION ltree IS 'data type for hierarchical tree-like structures';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';

--
-- Name: status; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
-- DROP TABLE IF EXISTS public.status CASCADE;

CREATE TABLE public.demo_user (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    city varchar(255),
    state varchar(255),
    address varchar(255),
    zip integer,
    sex varchar(255),
    avatar varchar(255),
    favorites varchar(255)[],
    created_dt timestamptz,
    updated_dt timestamptz,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.demo_user OWNER TO admin;

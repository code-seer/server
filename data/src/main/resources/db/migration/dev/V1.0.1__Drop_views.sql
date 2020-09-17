DROP VIEW IF EXISTS public.v_course_light CASCADE;
DROP VIEW IF EXISTS public.v_course CASCADE;


DROP TABLE IF EXISTS public.curriculum CASCADE;

CREATE TABLE public.curriculum (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    description text,
    status varchar(255),
    institution_id bigint,
    avatar varchar(255),
    num_requirements bigint,
    num_credits bigint,
    created_dt timestamptz,
    updated_dt timestamptz,
    FOREIGN KEY (institution_id) REFERENCES public.institution(id),
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.curriculum OWNER TO admin;

--
-- Name: course; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course CASCADE;

CREATE TABLE public.course (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    status varchar(255),
    subject varchar(255),
    level varchar(255),
    instruction_mode varchar (255),
    name varchar(255) NOT NULL,
    description text,
    requirements varchar(255)[],
    location varchar(255),
    num_credits bigint,
    price bigint,
    weekly_effort bigint,
    avatar varchar(255),
    learning_objectives varchar(255)[],
    short_name varchar(255),
    skills varchar(255)[],
    start_time_dt TIME with time zone,
    end_time_dt TIME with time zone,
    created_dt timestamptz,
    updated_dt timestamptz,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course OWNER TO admin;

--
-- Name: course_enrollment_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course_enrollment_bridge CASCADE;

CREATE TABLE public.course_enrollment_bridge (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     course_id bigint NOT NULL,
     student_uuid uuid NOT NULL,
     status varchar(255),
     created_dt timestamptz,
     updated_dt timestamptz,
    FOREIGN KEY (course_id) REFERENCES public.course(id),
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course_enrollment_bridge OWNER TO admin;

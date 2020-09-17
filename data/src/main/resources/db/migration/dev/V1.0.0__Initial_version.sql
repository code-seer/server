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

CREATE TABLE public.status (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   name varchar(255),
   _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.status OWNER TO admin;

--
-- Name: subject; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.subject CASCADE;

CREATE TABLE public.subject (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    path ltree,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.subject OWNER TO admin;
CREATE INDEX subject_path_idx ON subject USING gist (path);


--
-- Name: instruction_mode; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.instruction_mode CASCADE;

CREATE TABLE public.instruction_mode (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     name varchar(255),
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.instruction_mode OWNER TO admin;


--
-- Name: level; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.level CASCADE;

CREATE TABLE public.level (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    grade_level bigint,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.level OWNER TO admin;


--
-- Name: language; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.language CASCADE;

CREATE TABLE public.language (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     name varchar(255),
     code varchar(32),
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.language OWNER TO admin;

--
-- Name: outcome; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.outcome CASCADE;

CREATE TABLE public.outcome (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.outcome OWNER TO admin;


--
-- Name: institution; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.institution CASCADE;

CREATE TABLE public.institution (
    id SERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.institution OWNER TO admin;


--
-- Name: curriculum; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.curriculum CASCADE;

CREATE TABLE public.curriculum (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    description text,
    status_id bigint,
    institution_id bigint,
    avatar varchar(255),
    num_requirements bigint,
    num_credits bigint,
    created_dt timestamptz,
    updated_dt timestamptz,
    FOREIGN KEY (status_id) REFERENCES public.status(id),
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
    status_id bigint,
    subject_id bigint,
    level_id bigint,
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
    FOREIGN KEY (status_id) REFERENCES public.status(id),
    FOREIGN KEY (subject_id) REFERENCES public.subject(id),
    FOREIGN KEY (level_id) REFERENCES public.level(id),
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course OWNER TO admin;

--
-- Name: content; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.content CASCADE;

CREATE TABLE public.content (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    course_id bigint,
    sort_order bigint,
    name varchar(255),
    description text,
    urls text[],
    files text[],
    FOREIGN KEY (course_id) REFERENCES public.course(id),
    created_dt timestamptz,
    updated_dt timestamptz,
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.content OWNER TO admin;


--
-- Name: assignment; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.assignment CASCADE;

CREATE TABLE public.assignment (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   content_id bigint,
   sort_order bigint,
   name varchar(255),
   description text,
   urls text[],
   files text[],
   FOREIGN KEY (content_id) REFERENCES public.content(id),
   created_dt timestamptz,
   updated_dt timestamptz,
   _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.assignment OWNER TO admin;

--
-- Name: course_curriculum_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course_curriculum_bridge CASCADE;

CREATE TABLE public.course_curriculum_bridge (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     course_id bigint,
     curriculum_id bigint,
     FOREIGN KEY (course_id) REFERENCES public.course(id),
     FOREIGN KEY (curriculum_id) REFERENCES public.curriculum(id),
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course_curriculum_bridge OWNER TO admin;


--
-- Name: prerequisite; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.prerequisite CASCADE;

CREATE TABLE public.prerequisite (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    required_course_id bigint,
    requiring_course_id bigint,
    FOREIGN KEY (required_course_id) REFERENCES public.course(id),
    FOREIGN KEY (requiring_course_id) REFERENCES public.course(id),
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.prerequisite OWNER TO admin;


--
-- Name: faq; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.faq CASCADE;

CREATE TABLE public.faq (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    course_id bigint,
    question text,
    answer text,
    FOREIGN KEY (course_id) REFERENCES public.course(id),
    _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.faq OWNER TO admin;


--
-- Name: course_instruction_mode_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course_instruction_mode_bridge CASCADE;

CREATE TABLE public.course_instruction_mode_bridge (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   course_id bigint,
   instruction_mode_id bigint,
   FOREIGN KEY (course_id) REFERENCES public.course(id),
   FOREIGN KEY (instruction_mode_id) REFERENCES public.instruction_mode(id),
   _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course_instruction_mode_bridge OWNER TO admin;


--
-- Name: curriculum_instruction_mode_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.curriculum_instruction_mode_bridge CASCADE;

CREATE TABLE public.curriculum_instruction_mode_bridge (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   curriculum_id bigint,
   instruction_mode_id bigint,
   FOREIGN KEY (curriculum_id) REFERENCES public.curriculum(id),
   FOREIGN KEY (instruction_mode_id) REFERENCES public.instruction_mode(id),
   _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.curriculum_instruction_mode_bridge OWNER TO admin;


--
-- Name: curriculum_outcome_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.curriculum_outcome_bridge CASCADE;

CREATE TABLE public.curriculum_outcome_bridge (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  curriculum_id bigint,
  outcome_id bigint,
  FOREIGN KEY (curriculum_id) REFERENCES public.curriculum(id),
  FOREIGN KEY (outcome_id) REFERENCES public.outcome(id),
  _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.curriculum_outcome_bridge OWNER TO admin;


--
-- Name: course_language_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course_language_bridge CASCADE;

CREATE TABLE public.course_language_bridge (
   id BIGSERIAL PRIMARY KEY NOT NULL,
   course_id bigint,
   language_id bigint,
   FOREIGN KEY (course_id) REFERENCES public.course(id),
   FOREIGN KEY (language_id) REFERENCES public.language(id),
   _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course_language_bridge OWNER TO admin;


--
-- Name: course_enrollment_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course_enrollment_bridge CASCADE;

CREATE TABLE public.course_enrollment_bridge (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     course_id bigint NOT NULL,
     student_uuid uuid NOT NULL,
     status_id bigint NOT NULL,
     created_dt timestamptz,
     updated_dt timestamptz,
    FOREIGN KEY (course_id) REFERENCES public.course(id),
    FOREIGN KEY (status_id) REFERENCES public.status(id),
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course_enrollment_bridge OWNER TO admin;


--
-- Name: course_instructor_bridge; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP TABLE IF EXISTS public.course_instructor_bridge CASCADE;

CREATE TABLE public.course_instructor_bridge (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     course_id bigint NOT NULL,
     instructor_uuid uuid NOT NULL,
     FOREIGN KEY (course_id) REFERENCES public.course(id),
     _uuid uuid DEFAULT uuid_generate_v4 ()
);

ALTER TABLE public.course_instructor_bridge OWNER TO admin;

--
-- Name: v_course; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP VIEW IF EXISTS public.v_course CASCADE;
CREATE VIEW public.v_course AS
    SELECT
           c.id,
           s.name AS status,
           l.name AS level,
           sub.name AS subject,
           sub.path AS subject_path,
           cib.instructor_uuid as instructor_uuid,
           (SELECT
                   name
            FROM language
            WHERE language.id = clb.language_id) AS language,
           (SELECT
                   name
           FROM instruction_mode im
           WHERE im.id = cimb.instruction_mode_id) AS instruction_mode,
           ARRAY_AGG((
                SELECT
                       c2.name
                FROM course c2
                WHERE c2.id = p.required_course_id)
               ORDER BY (
                   SELECT
                       c2.id
                   FROM course c2
                   WHERE c2.id = p.required_course_id)) AS prereq_ids,
           p.required_course_id,
           c.name,
           c.description,
           c.requirements,
           c.location,
           c.num_credits,
           c.price,
           c.weekly_effort,
           c.avatar,
           c.learning_objectives,
           c.short_name,
           c.skills,
           c.start_time_dt,
           c.end_time_dt,
           c.created_dt,
           c.updated_dt,
           c._uuid as uuid
    FROM course c
        INNER JOIN status s ON c.status_id = s.id
        INNER JOIN level l ON c.level_id = l.id
        INNER JOIN subject sub ON c.subject_id = sub.id
        INNER JOIN course_language_bridge clb ON c.id = clb.course_id
        INNER JOIN course_instruction_mode_bridge cimb ON c.id = cimb.course_id
        INNER JOIN course_instructor_bridge cib ON c.id = cib.course_id
        INNER JOIN prerequisite p ON c.id = p.requiring_course_id
    GROUP BY
             c.id,
             s.name,
             l.name,
             sub.name,
             sub.path,
             cib.instructor_uuid,
             clb.language_id,
             cimb.id,
             p.required_course_id,
             c.name,
             c.description,
             c.requirements,
             c.location,
             c.num_credits,
             c.price,
             c.weekly_effort,
             c.avatar,
             c.learning_objectives,
             c.short_name,
             c.skills,
             c.start_time_dt,
             c.end_time_dt,
             c.created_dt,
             c.updated_dt,
             c._uuid
    ORDER BY c.name ASC;

--
-- Name: v_course_light; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP VIEW IF EXISTS public.v_course_light CASCADE;
CREATE VIEW public.v_course_light AS
    SELECT
           vc.id,
           vc.uuid,
           vc.status,
           vc.subject,
           vc.instructor_uuid,
           vc.instruction_mode,
           vc.language,
           vc.level,
           vc.name,
           vc.description,
           vc.location,
           vc.num_credits,
           vc.price,
           vc.weekly_effort,
           vc.avatar,
           vc.short_name,
           vc.start_time_dt,
           vc.end_time_dt,
           vc.created_dt,
           vc.updated_dt
    FROM v_course vc
    ORDER BY vc.name ASC;

--
-- Name: v_curriculum; Type: TABLE; Schema: public; Owner: -; Tablespace:
--
DROP VIEW IF EXISTS public.v_curriculum CASCADE;
CREATE VIEW public.v_curriculum AS
    SELECT
        cur.id,
        cur.name,
        cur.description,
        s.name AS status,
        i.name AS institution,
        (SELECT
             name
         FROM instruction_mode im
         WHERE im.id = cimb.instruction_mode_id) AS instruction_mode,
        ccb.id AS course_id,
        cur.avatar,
        cur.num_requirements,
        cur.num_credits,
        cur.created_dt,
        cur.updated_dt,
        cur._uuid as uud
    FROM curriculum cur
             INNER JOIN status s ON cur.status_id = s.id
             INNER JOIN curriculum_instruction_mode_bridge cimb ON cur.id = cimb.curriculum_id
             INNER JOIN institution i ON cur.institution_id = i.id
             INNER JOIN curriculum_outcome_bridge cob ON cur.id = cob.curriculum_id
             INNER JOIN course_curriculum_bridge ccb ON cur.id = ccb.curriculum_id
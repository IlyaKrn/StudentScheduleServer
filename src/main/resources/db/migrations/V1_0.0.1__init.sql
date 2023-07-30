-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL,
    banned boolean NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

-- Table: public.user_roles

-- DROP TABLE IF EXISTS public.user_roles;

CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id bigint NOT NULL,
    roles integer,
    CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_roles
    OWNER to postgres;

-- Table: public.specific_lessons

-- DROP TABLE IF EXISTS public.specific_lessons;

CREATE TABLE IF NOT EXISTS public.specific_lessons
(
    id bigint NOT NULL,
    canceled boolean NOT NULL,
    group_id bigint NOT NULL,
    lesson_id bigint NOT NULL,
    "time" bigint NOT NULL,
    CONSTRAINT specific_lessons_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.specific_lessons
    OWNER to postgres;


-- Table: public.specific_lesson_medias

-- DROP TABLE IF EXISTS public.specific_lesson_medias;

CREATE TABLE IF NOT EXISTS public.specific_lesson_medias
(
    id bigint NOT NULL,
    specific_lesson_id bigint NOT NULL,
    url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT specific_lesson_medias_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.specific_lesson_medias
    OWNER to postgres;


-- Table: public.specific_lesson_media_comments

-- DROP TABLE IF EXISTS public.specific_lesson_media_comments;

CREATE TABLE IF NOT EXISTS public.specific_lesson_media_comments
(
    id bigint NOT NULL,
    media_id bigint NOT NULL,
    question_comment_id bigint,
    text character varying(255) COLLATE pg_catalog."default" NOT NULL,
    author_id bigint NOT NULL,
    CONSTRAINT specific_lesson_media_comments_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.specific_lesson_media_comments
    OWNER to postgres;

-- Table: public.schedule_templates

-- DROP TABLE IF EXISTS public.schedule_templates;

CREATE TABLE IF NOT EXISTS public.schedule_templates
(
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    time_start bigint NOT NULL,
    time_stop bigint NOT NULL,
    CONSTRAINT schedule_templates_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.schedule_templates
    OWNER to postgres;

-- Table: public.members

-- DROP TABLE IF EXISTS public.members;

CREATE TABLE IF NOT EXISTS public.members
(
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT members_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.members
    OWNER to postgres;

-- Table: public.member_roles

-- DROP TABLE IF EXISTS public.member_roles;

CREATE TABLE IF NOT EXISTS public.member_roles
(
    member_id bigint NOT NULL,
    roles integer,
    CONSTRAINT fk431yrnsn5s4omvwjvl9dre1n0 FOREIGN KEY (member_id)
        REFERENCES public.members (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.member_roles
    OWNER to postgres;


-- Table: public.lesson_templates

-- DROP TABLE IF EXISTS public.lesson_templates;

CREATE TABLE IF NOT EXISTS public.lesson_templates
(
    id bigint NOT NULL,
    lesson_id bigint NOT NULL,
    schedule_template_id bigint NOT NULL,
    "time" bigint NOT NULL,
    CONSTRAINT lesson_templates_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lesson_templates
    OWNER to postgres;


-- Table: public.groups

-- DROP TABLE IF EXISTS public.groups;

CREATE TABLE IF NOT EXISTS public.groups
(
    id bigint NOT NULL,
    chat_id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.groups
    OWNER to postgres;


-- Table: public.custom_lessons

-- DROP TABLE IF EXISTS public.custom_lessons;

CREATE TABLE IF NOT EXISTS public.custom_lessons
(
    id bigint NOT NULL,
    group_id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    teacher character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT custom_lessons_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.custom_lessons
    OWNER to postgres;




-- Table: public.service_tokens

-- DROP TABLE IF EXISTS public.service_tokens;

CREATE TABLE IF NOT EXISTS public.service_tokens
(
    id bigint NOT NULL,
    service_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    service_token character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT service_tokens_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.service_tokens
    OWNER to postgres;
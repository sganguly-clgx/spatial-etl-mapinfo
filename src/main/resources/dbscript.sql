-- Table: public.cdscustomterritory

-- DROP TABLE public.cdscustomterritory;

CREATE TABLE public.cdscustomterritory
(
  customlayertag character varying(100),
  statecd character(2),
  attributes jsonb,
  geom geometry(Geometry,4326)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.cdscustomterritory
  OWNER TO postgres;

-- Index: public.cdscustomterritory_idx

-- DROP INDEX public.cdscustomterritory_idx;

CREATE INDEX cdscustomterritory_idx
  ON public.cdscustomterritory
  USING gist
  (geom);

-- Index: public.cdscustomterritory_idx1

-- DROP INDEX public.cdscustomterritory_idx1;

CREATE INDEX cdscustomterritory_idx1
  ON public.cdscustomterritory
  USING btree
  (customlayertag COLLATE pg_catalog."default");


create schema tracking;


CREATE TABLE public.debezium_offset_storage (
	id varchar(36) NOT NULL,
	offset_key varchar(1255) NULL,
	offset_val varchar(1255) NULL,
	record_insert_ts timestamp NOT NULL,
	record_insert_seq int4 NOT NULL
);

ALTER TABLE debezium_offset_storage  REPLICA IDENTITY FULL;
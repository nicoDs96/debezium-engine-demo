#!/bin/bash
set -e
set -x


psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL

create schema tracking;

CREATE TABLE public.debezium_offset_storage (
	id varchar(36) PRIMARY KEY,
	offset_key varchar(1255) NULL,
	offset_val varchar(1255) NULL,
	record_insert_ts timestamp NOT NULL,
	record_insert_seq int4 NOT NULL
);

CREATE TABLE public.dummy_table (
	username text NOT NULL PRIMARY KEY,
	address text
);
EOSQL
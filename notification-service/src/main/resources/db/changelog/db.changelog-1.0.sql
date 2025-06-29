--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS user_devices(
    user_id UUID,
    device_token TEXT NOT NULL,
    device_type VARCHAR(16) NOT NULL
);
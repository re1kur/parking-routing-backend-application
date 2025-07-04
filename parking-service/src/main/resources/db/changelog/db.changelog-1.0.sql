--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS regions
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS region_codes
(
    code      VARCHAR(3) PRIMARY KEY,
    region_id SMALLINT NOT NULL,
    FOREIGN KEY (region_id) REFERENCES regions (id)
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS makes
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

--changeset re1kur:4
CREATE TABLE IF NOT EXISTS cars
(
    id            uuid PRIMARY KEY,
    owner_id      uuid        NOT NULL,
    license_plate VARCHAR(10) NOT NULL,
    region_code   VARCHAR(3)  NOT NULL,
    UNIQUE (license_plate, region_code),
    FOREIGN KEY (region_code) REFERENCES region_codes (code)
);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS car_information
(
    car_id  uuid PRIMARY KEY,
    make_id SMALLINT    NOT NULL,
    color   VARCHAR(16) NOT NULL,
    model   VARCHAR(32) NOT NULL,
    FOREIGN KEY (make_id) REFERENCES makes (id),
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
);

--changeset re1kur:6
CREATE TABLE IF NOT EXISTS car_images
(
    car_id  uuid,
    file_id VARCHAR(128),
    PRIMARY KEY (car_id, file_id),
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
);

--changeset re1kur:7
CREATE INDEX idx_owner_id ON cars (owner_id);
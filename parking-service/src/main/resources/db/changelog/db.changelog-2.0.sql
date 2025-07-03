--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS parking_places
(
    number    SMALLINT PRIMARY KEY,
    latitude  NUMERIC(9, 6) NOT NULL,
    longitude NUMERIC(9, 6) NOT NULL,
    UNIQUE (latitude, longitude)
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS parking_place_reservations
(
    id               uuid PRIMARY KEY,
    place_number     SMALLINT                 NOT NULL,
    occupant_user_id uuid                     NOT NULL,
    reserved_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    ends_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    is_paid          BOOLEAN                  NOT NULL DEFAULT FALSE,
    FOREIGN KEY (place_number) REFERENCES parking_places (number)
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS parking_place_information
(
    parking_place_number SMALLINT PRIMARY KEY,
    occupant_car_id      uuid,
    reservation_id       uuid,
    is_available         BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (occupant_car_id) REFERENCES cars (id) ON DELETE SET NULL,
    FOREIGN KEY (reservation_id) REFERENCES parking_place_reservations (id) ON DELETE SET NULL
);
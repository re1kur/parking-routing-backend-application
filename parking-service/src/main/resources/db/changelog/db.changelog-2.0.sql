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
CREATE TABLE IF NOT EXISTS reservations
(
    id UUID PRIMARY KEY,
    place_number SMALLINT NOT NULL,
    user_id UUID NOT NULL,
    car_id UUID NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (place_number) REFERENCES parking_places(number),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS reservation_information
(
    reservation_id UUID PRIMARY KEY,
    reserved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

--changeset re1kur:4
CREATE TABLE IF NOT EXISTS parking_sessions
(
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL,
    actual_started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actual_ended_at TIMESTAMP DEFAULT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS parking_reports
(
    session_id UUID PRIMARY KEY,
    payment_id UUID ,
    enter_image_id VARCHAR(128),
    exit_image_id VARCHAR(128),
    FOREIGN KEY (session_id) REFERENCES parking_sessions(id)
);

--changeset re1kur:6
CREATE INDEX idx_reservation_car_id ON reservations (car_id)

--changeset re1kur:7
CREATE INDEX idx_parking_session_reservation_id ON parking_sessions (reservation_id)
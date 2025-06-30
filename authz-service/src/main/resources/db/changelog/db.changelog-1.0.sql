--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS services
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS endpoints
(
    id          SMALLSERIAL PRIMARY KEY,
    service_id  SMALLINT    NOT NULL,
    method_name VARCHAR(64) NOT NULL,
    method_type VARCHAR(64) NOT NULL CHECK (method_type IN ('GET', 'POST', 'PUT', 'DELETE')),
    FOREIGN KEY (service_id) REFERENCES services (id) ON DELETE CASCADE,
    UNIQUE (service_id, method_name, method_type)
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS access_rules
(
    endpoint_id SMALLINT NOT NULL,
    role        VARCHAR(32),
    PRIMARY KEY (endpoint_id, role),
    FOREIGN KEY (endpoint_id) REFERENCES endpoints (id) ON DELETE CASCADE
);

--changeset re1kur:4
CREATE INDEX idx_endpoints_service_method_name_type
    ON endpoints (service_id, method_name, method_type);

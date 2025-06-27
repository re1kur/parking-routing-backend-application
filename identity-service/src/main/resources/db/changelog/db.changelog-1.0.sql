-- liquibase formatted sql

-- changeset re1kur:1
CREATE TABLE IF NOT EXISTS users
(
    id           uuid PRIMARY KEY             DEFAULT gen_random_uuid(),
    email        VARCHAR(256) UNIQUE NOT NULL,
    phone_number CHAR(10) UNIQUE     NOT NULL CHECK (phone_number ~ '^\d{10}$'),
    enabled      BOOLEAN             NOT NULL DEFAULT FALSE
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS users_roles
(
    user_id uuid,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

--changeset re1kur:4
CREATE INDEX idx_users_roles_user_id ON users_roles (user_id);
CREATE INDEX idx_users_roles_role_id ON users_roles (role_id);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS user_information
(
    user_id     UUID PRIMARY KEY,
    first_name  VARCHAR(64) NOT NULL,
    last_name   VARCHAR(64) NOT NULL,
    middle_name VARCHAR(64),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
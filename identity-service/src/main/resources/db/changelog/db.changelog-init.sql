--liquibase formatted sql

--changeset re1kur:1
INSERT INTO roles(name)
VALUES ('MODERATOR');

--changeset re1kur:2
INSERT INTO roles(name)
VALUES ('OPERATOR');
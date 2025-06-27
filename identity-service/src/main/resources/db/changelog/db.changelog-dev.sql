--liquibase formatted sql

--changeset re1kur:1
INSERT INTO users(id, email, phone_number, enabled)
VALUES ('11111111-1111-1111-1111-111111111111', 'moderator@mail.com',
        '4392313419', true);

--changeset re1kur:2
INSERT INTO users(id, email, phone_number, enabled)
VALUES ('22222222-2222-2222-2222-222222222222', 'user@mail.com',
        '4322311238', true);

--changeset re1kur:3
INSERT INTO users_roles(user_id, role_id)
VALUES ('11111111-1111-1111-1111-111111111111', (select id from roles where name = 'MODERATOR'));
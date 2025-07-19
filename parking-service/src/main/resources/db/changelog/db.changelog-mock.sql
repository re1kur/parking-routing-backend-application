--liquibase formatted sql

--changeset re1kur:1
INSERT INTO cars (id, owner_id, license_plate, region_code)
VALUES
('c1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'A111AA', '77'),
('c2222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 'B222BB', '178'),
('c3333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 'C333CC', '63');

INSERT INTO car_information (car_id, make_id, color, model)
VALUES
('c1111111-1111-1111-1111-111111111111', (SELECT id FROM makes WHERE name = 'BMW'), 'Black', 'X5'),
('c2222222-2222-2222-2222-222222222222', (SELECT id FROM makes WHERE name = 'Toyota'), 'White', 'Camry'),
('c3333333-3333-3333-3333-333333333333', (SELECT id FROM makes WHERE name = 'Lada'), 'Red', 'Vesta');


--changeset re1kur:2
INSERT INTO parking_places (number, latitude, longitude)
VALUES
(1, 55.755826, 37.617300),
(2, 55.751244, 37.618423),
(3, 55.760000, 37.615000),
(4, 55.770000, 37.620000),
(5, 55.765000, 37.610000);

--changeset re1kur:3
INSERT INTO reservations (id, place_number, user_id, car_id, paid)
VALUES
('a1111111-0000-0000-0000-000000000001', 1, '11111111-1111-1111-1111-111111111111', 'c1111111-1111-1111-1111-111111111111', true),
('a1111111-0000-0000-0000-000000000002', 2, '22222222-2222-2222-2222-222222222222', 'c2222222-2222-2222-2222-222222222222', false),
('a1111111-0000-0000-0000-000000000003', 3, '22222222-2222-2222-2222-222222222222', 'c3333333-3333-3333-3333-333333333333', true);

--changeset re1kur:4
INSERT INTO reservation_information (reservation_id, reserved_at, start_at, end_at)
VALUES
('a1111111-0000-0000-0000-000000000001', now() - interval '2 days', now() - interval '1 day', now() + interval '1 day'),
('a1111111-0000-0000-0000-000000000002', now() - interval '1 day', now(), now() + interval '1 day'),
('a1111111-0000-0000-0000-000000000003', now() - interval '3 days', now() - interval '2 days', now() - interval '1 day');

--changeset re1kur:5
INSERT INTO parking_sessions (id, reservation_id, actual_started_at, actual_ended_at, paid)
VALUES
('b1111111-0000-0000-0000-000000000001', 'a1111111-0000-0000-0000-000000000001', now() - interval '1 day', null, true),
('b1111111-0000-0000-0000-000000000002', 'a1111111-0000-0000-0000-000000000002', now(), null, false),
('b1111111-0000-0000-0000-000000000003', 'a1111111-0000-0000-0000-000000000003', now() - interval '2 days', now() - interval '1 day', true);


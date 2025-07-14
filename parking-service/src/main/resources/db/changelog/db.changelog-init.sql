--liquibase formatted sql

--changeset re1kur:1
INSERT INTO makes (name)
VALUES ('Toyota'),
       ('Hyundai'),
       ('Kia'),
       ('Lada'),
       ('Volkswagen'),
       ('Skoda'),
       ('Renault'),
       ('Nissan'),
       ('Ford'),
       ('BMW'),
       ('Mercedes-Benz'),
       ('Audi'),
       ('Chevrolet'),
       ('Mazda'),
       ('Mitsubishi'),
       ('Honda'),
       ('Lexus'),
       ('Peugeot'),
       ('Suzuki'),
       ('Geely');

--changeset re1kur:2
INSERT INTO regions (name)
VALUES ('Москва'),
       ('Санкт-Петербург'),
       ('Московская область'),
       ('Краснодарский край'),
       ('Свердловская область'),
       ('Ростовская область'),
       ('Республика Татарстан'),
       ('Челябинская область'),
       ('Нижегородская область'),
       ('Башкортостан'),
       ('Самарская область'),
       ('Новосибирская область'),
       ('Ставропольский край'),
       ('Красноярский край'),
       ('Иркутская область'),
       ('Воронежская область'),
       ('Волгоградская область'),
       ('Ханты-Мансийский АО'),
       ('Пермский край'),
       ('Алтайский край'),
       ('Республика Саха (Якутия)'),
       ('Хабаровский край'),
       ('Приморский край'),
       ('Республика Дагестан'),
       ('Омская область'),
       ('Оренбургская область'),
       ('Белгородская область'),
       ('Тюменская область'),
       ('Курская область'),
       ('Липецкая область'),
       ('Калининградская область'),
       ('Кемеровская область'),
       ('Саратовская область'),
       ('Ульяновская область'),
       ('Республика Марий Эл'),
       ('Республика Мордовия'),
       ('Республика Карелия');


-- changeset re1kur:3
WITH
moscow AS (SELECT id FROM regions WHERE name = 'Москва'),
saint_peter AS (SELECT id FROM regions WHERE name = 'Санкт-Петербург'),
moscow_district AS (SELECT id FROM regions WHERE name = 'Московская область'),
krasnodar AS (SELECT id FROM regions WHERE name = 'Краснодарский край'),
sverdlovsk AS (SELECT id FROM regions WHERE name = 'Свердловская область'),
rostov AS (SELECT id FROM regions WHERE name = 'Ростовская область'),
tatarstan AS (SELECT id FROM regions WHERE name = 'Республика Татарстан'),
chelyabinsk AS (SELECT id FROM regions WHERE name = 'Челябинская область'),
nizhny AS (SELECT id FROM regions WHERE name = 'Нижегородская область'),
bashkortostan AS (SELECT id FROM regions WHERE name = 'Башкортостан'),
samara AS (SELECT id FROM regions WHERE name = 'Самарская область'),
novosibirsk AS (SELECT id FROM regions WHERE name = 'Новосибирская область'),
stavropol AS (SELECT id FROM regions WHERE name = 'Ставропольский край'),
krasnoyarsk AS (SELECT id FROM regions WHERE name = 'Красноярский край'),
irkutsk AS (SELECT id FROM regions WHERE name = 'Иркутская область'),
voronezh AS (SELECT id FROM regions WHERE name = 'Воронежская область'),
volgograd AS (SELECT id FROM regions WHERE name = 'Волгоградская область'),
hmao AS (SELECT id FROM regions WHERE name = 'Ханты-Мансийский АО'),
perm AS (SELECT id FROM regions WHERE name = 'Пермский край'),
altai AS (SELECT id FROM regions WHERE name = 'Алтайский край')
INSERT INTO region_codes (code, region_id)
VALUES
-- Москва
('77', (SELECT id FROM moscow)), ('97', (SELECT id FROM moscow)), ('99', (SELECT id FROM moscow)),
('177', (SELECT id FROM moscow)), ('197', (SELECT id FROM moscow)), ('199', (SELECT id FROM moscow)),
('777', (SELECT id FROM moscow)), ('799', (SELECT id FROM moscow)),

-- Санкт-Петербург
('78', (SELECT id FROM saint_peter)), ('98', (SELECT id FROM saint_peter)),
('178', (SELECT id FROM saint_peter)), ('198', (SELECT id FROM saint_peter)),

-- Московская область
('50', (SELECT id FROM moscow_district)), ('90', (SELECT id FROM moscow_district)),
('150', (SELECT id FROM moscow_district)), ('190', (SELECT id FROM moscow_district)),
('750', (SELECT id FROM moscow_district)), ('790', (SELECT id FROM moscow_district)),

-- Краснодарский край
('23', (SELECT id FROM krasnodar)), ('93', (SELECT id FROM krasnodar)),
('123', (SELECT id FROM krasnodar)), ('193', (SELECT id FROM krasnodar)),

-- Свердловская область
('66', (SELECT id FROM sverdlovsk)), ('96', (SELECT id FROM sverdlovsk)), ('196', (SELECT id FROM sverdlovsk)),

-- Ростовская область
('61', (SELECT id FROM rostov)), ('161', (SELECT id FROM rostov)),

-- Татарстан
('16', (SELECT id FROM tatarstan)), ('116', (SELECT id FROM tatarstan)), ('716', (SELECT id FROM tatarstan)),

-- Челябинская область
('74', (SELECT id FROM chelyabinsk)), ('174', (SELECT id FROM chelyabinsk)),

-- Нижегородская область
('52', (SELECT id FROM nizhny)), ('152', (SELECT id FROM nizhny)),

-- Башкортостан
('02', (SELECT id FROM bashkortostan)), ('102', (SELECT id FROM bashkortostan)), ('702', (SELECT id FROM bashkortostan)),

-- Самарская область
('63', (SELECT id FROM samara)), ('163', (SELECT id FROM samara)),

-- Новосибирская область
('54', (SELECT id FROM novosibirsk)), ('154', (SELECT id FROM novosibirsk)),

-- Ставропольский край
('26', (SELECT id FROM stavropol)), ('126', (SELECT id FROM stavropol)),

-- Красноярский край
('24', (SELECT id FROM krasnoyarsk)), ('124', (SELECT id FROM krasnoyarsk)),

-- Иркутская область
('38', (SELECT id FROM irkutsk)), ('138', (SELECT id FROM irkutsk)),

-- Воронежская область
('36', (SELECT id FROM voronezh)), ('136', (SELECT id FROM voronezh)),

-- Волгоградская область
('34', (SELECT id FROM volgograd)), ('134', (SELECT id FROM volgograd)),

-- Ханты-Мансийский АО
('86', (SELECT id FROM hmao)), ('186', (SELECT id FROM hmao)),

-- Пермский край
('59', (SELECT id FROM perm)), ('159', (SELECT id FROM perm)),

-- Алтайский край
('22', (SELECT id FROM altai)), ('122', (SELECT id FROM altai)),

-- Республика Саха (Якутия)
('14', (SELECT id FROM regions WHERE name = 'Республика Саха (Якутия)')),
('114', (SELECT id FROM regions WHERE name = 'Республика Саха (Якутия)')),

-- Хабаровский край
('27', (SELECT id FROM regions WHERE name = 'Хабаровский край')),
('127', (SELECT id FROM regions WHERE name = 'Хабаровский край')),

-- Приморский край
('25', (SELECT id FROM regions WHERE name = 'Приморский край')),
('125', (SELECT id FROM regions WHERE name = 'Приморский край')),

-- Республика Дагестан
('05', (SELECT id FROM regions WHERE name = 'Республика Дагестан')),
('105', (SELECT id FROM regions WHERE name = 'Республика Дагестан')),

-- Омская область
('55', (SELECT id FROM regions WHERE name = 'Омская область')),
('155', (SELECT id FROM regions WHERE name = 'Омская область')),

-- Оренбургская область
('56', (SELECT id FROM regions WHERE name = 'Оренбургская область')),
('156', (SELECT id FROM regions WHERE name = 'Оренбургская область')),

-- Белгородская область
('31', (SELECT id FROM regions WHERE name = 'Белгородская область')),
('131', (SELECT id FROM regions WHERE name = 'Белгородская область')),

-- Тюменская область
('72', (SELECT id FROM regions WHERE name = 'Тюменская область')),
('172', (SELECT id FROM regions WHERE name = 'Тюменская область')),

-- Курская область
('46', (SELECT id FROM regions WHERE name = 'Курская область')),
('146', (SELECT id FROM regions WHERE name = 'Курская область')),

-- Липецкая область
('48', (SELECT id FROM regions WHERE name = 'Липецкая область')),
('148', (SELECT id FROM regions WHERE name = 'Липецкая область')),

-- Калининградская область
('39', (SELECT id FROM regions WHERE name = 'Калининградская область')),
('139', (SELECT id FROM regions WHERE name = 'Калининградская область')),

-- Кемеровская область
('42', (SELECT id FROM regions WHERE name = 'Кемеровская область')),
('142', (SELECT id FROM regions WHERE name = 'Кемеровская область')),

-- Саратовская область
('64', (SELECT id FROM regions WHERE name = 'Саратовская область')),
('164', (SELECT id FROM regions WHERE name = 'Саратовская область')),

-- Ульяновская область
('73', (SELECT id FROM regions WHERE name = 'Ульяновская область')),
('173', (SELECT id FROM regions WHERE name = 'Ульяновская область')),

-- Республика Марий Эл
('12', (SELECT id FROM regions WHERE name = 'Республика Марий Эл')),
('112', (SELECT id FROM regions WHERE name = 'Республика Марий Эл')),

-- Республика Мордовия
('13', (SELECT id FROM regions WHERE name = 'Республика Мордовия')),
('113', (SELECT id FROM regions WHERE name = 'Республика Мордовия')),

-- Республика Карелия
('10', (SELECT id FROM regions WHERE name = 'Республика Карелия')),
('110', (SELECT id FROM regions WHERE name = 'Республика Карелия'))

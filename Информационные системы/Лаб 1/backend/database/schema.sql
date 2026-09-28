CREATE TABLE IF NOT EXISTS app_user (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY CHECK (id > 0),
    login varchar(100) NOT NULL UNIQUE CHECK (btrim(login) <> ''),
    password_hash varchar(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS discipline (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY CHECK (id > 0),
    name varchar(255) NOT NULL CHECK (btrim(name) <> ''),
    practice_hours bigint NOT NULL,
    self_study_hours bigint NOT NULL,
    labs_count integer
);

CREATE TABLE IF NOT EXISTS person (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY CHECK (id > 0),
    name varchar(255) NOT NULL CHECK (btrim(name) <> ''),
    eye_color varchar(16) NOT NULL CHECK (eye_color IN ('GREEN', 'YELLOW', 'ORANGE', 'WHITE')),
    hair_color varchar(16) CHECK (hair_color IN ('GREEN', 'YELLOW', 'ORANGE', 'WHITE')),
    location_x bigint NOT NULL,
    location_y double precision NOT NULL,
    location_z bigint NOT NULL,
    location_name varchar(500) NOT NULL,
    birthday timestamp NOT NULL,
    weight bigint CHECK (weight > 0),
    nationality varchar(16) NOT NULL CHECK (nationality IN ('USA', 'SPAIN', 'NORTH_KOREA', 'JAPAN'))
);

CREATE TABLE IF NOT EXISTS lab_work (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY CHECK (id > 0),
    version bigint NOT NULL DEFAULT 0 CHECK (version >= 0),
    name varchar(255) NOT NULL CHECK (btrim(name) <> ''),
    coordinate_x real NOT NULL CHECK (coordinate_x > -523 AND coordinate_x <> 'NaN'::real),
    coordinate_y integer NOT NULL CHECK (coordinate_y > -643),
    creation_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description varchar(3429) NOT NULL,
    difficulty varchar(16) NOT NULL CHECK (difficulty IN ('EASY', 'IMPOSSIBLE', 'INSANE', 'HOPELESS')),
    discipline_id bigint REFERENCES discipline(id),
    minimal_point double precision NOT NULL CHECK (minimal_point > 0 AND minimal_point <> 'NaN'::double precision),
    average_point integer NOT NULL CHECK (average_point > 0),
    author_id bigint REFERENCES person(id)
);

-- Повторное применение схемы обновляет базы, созданные до появления версий.
ALTER TABLE lab_work ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;

CREATE INDEX IF NOT EXISTS lab_work_discipline_idx ON lab_work(discipline_id);
CREATE INDEX IF NOT EXISTS lab_work_author_idx ON lab_work(author_id);

-- Локальные демонстрационные данные. Удаляет также все аккаунты приложения.
BEGIN;

TRUNCATE TABLE lab_work, person, discipline, app_user RESTART IDENTITY;

INSERT INTO discipline (name, practice_hours, self_study_hours, labs_count) VALUES
    ('Программирование', 36, 72, 4),
    ('Базы данных', 24, 48, 3),
    ('Веб-разработка', 32, 64, 4),
    ('Математика', 20, 40, 2);

INSERT INTO person (
    name, eye_color, hair_color, location_x, location_y, location_z,
    location_name, birthday, weight, nationality
) VALUES
    ('Anna Smith', 'GREEN', 'ORANGE', 12, 3.5, 0, 'New York', '1998-05-12 00:00:00', 58, 'USA'),
    ('Lucia Garcia', 'YELLOW', 'WHITE', -8, 1.25, 2, 'Madrid', '1997-09-03 00:00:00', 62, 'SPAIN'),
    ('Haruto Sato', 'WHITE', 'GREEN', 24, -6.75, 1, 'Tokyo', '2000-01-19 00:00:00', 71, 'JAPAN'),
    ('Kim Min-jun', 'ORANGE', NULL, 5, 10.0, -1, 'Pyongyang', '1999-11-27 00:00:00', NULL, 'NORTH_KOREA');

INSERT INTO lab_work (
    name, coordinate_x, coordinate_y, description, difficulty,
    discipline_id, minimal_point, average_point, author_id
) VALUES
    ('Введение в Java', 10.5, 20, 'Классы, поля и методы.', 'EASY', 1, 20.0, 72, 1),
    ('Коллекции Java', -15.0, 34, 'Списки, множества и отображения.', 'INSANE', 1, 35.5, 68, 3),
    ('SQL-запросы', 4.0, -12, 'Выборка, соединения и группировка.', 'EASY', 2, 25.0, 81, 2),
    ('Нормализация БД', 18.25, 7, 'Нормальные формы и зависимости.', 'IMPOSSIBLE', 2, 40.0, 75, 2),
    ('REST API', -30.0, 15, 'Маршруты и ответы HTTP.', 'INSANE', 3, 30.0, 77, 1),
    ('Клиент на React', 42.0, -8, 'Формы и работа с API.', 'HOPELESS', 3, 45.0, 69, 4),
    ('Численные методы', -100.5, 50, 'Приближенное решение уравнений.', 'IMPOSSIBLE', 4, 28.0, 73, 3),
    ('Теория графов', 3.75, -25, 'Поиск путей и обход графов.', 'HOPELESS', 4, 32.0, 66, 4);

COMMIT;

SELECT
    (SELECT count(*) FROM app_user) AS users,
    (SELECT count(*) FROM person) AS authors,
    (SELECT count(*) FROM discipline) AS disciplines,
    (SELECT count(*) FROM lab_work) AS lab_works;

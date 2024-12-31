import sqlite3
from sqlite3 import Error


def create_connection(path):
    connection = None
    try:
        connection = sqlite3.connect(path)
        print("Connection to SQLite DB successful")
    except Error as e:
        print(f"The error '{e}' occurred")

    return connection

def execute_query(connection, query):
    cursor = connection.cursor()
    try:
        cursor.execute(query)
        connection.commit()
        print("Query executed successfully")
    except Error as e:
        print(f"The error '{e}' occurred")

def execute_read_query(connection, query):
    cursor = connection.cursor()
    result = None
    try:
        cursor.execute(query)
        result = cursor.fetchall()
        return result
    except Error as e:
        print(f"The error '{e}' occurred")

connection = create_connection("C://Users//user/PycharmProjects/lab4/lab4.sql")


#Создайте таблицы с помощью Python для SQLite и MySQL.
create_character_table = """
CREATE TABLE IF NOT EXISTS characters (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT,
  age INTEGER,
  weapon TEXT,
  grade TEXT,
  sorcerer_id INTEGER
);
"""

create_technique_table = """
CREATE TABLE IF NOT EXISTS techniques (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  cursed_technique TEXT,
  sorcerer_id INTEGER,
  FOREIGN KEY (sorcerer_id) REFERENCES characters (id)
);
"""

create_clan_table = """
CREATE TABLE IF NOT EXISTS clans (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  sorcerer_clan TEXT,
  member_count INTEGER,
  head_clan TEXT,
  FOREIGN KEY (head_clan) REFERENCES character (name)
);
"""

create_domain_expansion_table = """
CREATE TABLE IF NOT EXISTS domain_expansions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  domain TEXT,
  skill INTEGER,
  type TEXT,
  sorcerer_id INTEGER,
  FOREIGN KEY (sorcerer_id) REFERENCES character (id)
);
"""


execute_query(connection, create_character_table)
execute_query(connection, create_technique_table)
execute_query(connection, create_clan_table)
execute_query(connection, create_domain_expansion_table)


create_characters  = """
INSERT INTO
  characters (name, age, weapon, grade, sorcerer_id)
VALUES
  ('Satoru', 28, 'Six Eyes', 'Special', 1),
  ('Suguru', 27, 'Curses', 'Special', 2),
  ('Nanami', 27, 'Sword', 'First', 3),
  ('Yuta', 17, 'Katana', 'Special', 4),
  ('Toji', 21, 'Nunchaki', 'Special', 5);
"""

create_techniques = """
INSERT INTO
    techniques (cursed_technique, sorcerer_id)
VALUES
    ('Purple', 1),
    ('Curses absorption', 2),
    ('Symmetry', 3),
    ('Copying', 4),
    ('None', 5);
"""

create_clans = """
INSERT INTO
    clans (sorcerer_clan, member_count, head_clan)
VALUES 
    ('Gojo', 7, 'Satoru'),
    ('Zenin', 25, 'Megumi'),
    ('Kamo', 13, 'Kenjaku'),
    ('Inumaki', 4, 'Inumaki'),
    ('Fujiwara', 16, 'Takako');
"""

create_domain_expansions = """
INSERT INTO
    domain_expansions (domain, skill, type, sorcerer_id)
VALUES 
    ('Infinite void', 1000, 'impact on perception', 1),
    ('True and mutual love', 687, 'copying techniques', 4),
    ('Demon tomb', 999, 'damage', 6),
    ('Shadow Garden of the Chimera', 548, 'i dont remember', 8),
    ('Self-embodiment of the Ideal', 845, 'dont remember this too', 9);
"""

execute_query(connection, create_characters)
execute_query(connection, create_techniques)
execute_query(connection, create_domain_expansions)
execute_query(connection, create_clans)

#Добавьте по одной новой записи в каждую из  таблиц Вашей базы данных.
add_characters = """
INSERT INTO characters(name, age, weapon, grade, sorcerer_id)
VALUES 
    ('Sukuna', 10000, 'None', 'Special', 6)
"""

add_technique = """
INSERT INTO techniques(cursed_technique, sorcerer_id)
VALUES 
    ('Scissors', 6)
"""

add_clan = """
INSERT INTO clans(sorcerer_clan, member_count, head_clan)
VALUES
    ('Geto', 100, 'Suguru')
"""

add_expansion = """
INSERT INTO domain_expansions(domain, skill, type, sorcerer_id)
VALUES
    ('Garden of Edem', 574, 'illusions', 7)
"""

execute_query(connection, add_characters)
execute_query(connection, add_technique)
execute_query(connection, add_clan)
execute_query(connection, add_expansion)

print()

#составить запрос по извлечению данных с использованием WHERE и GROUP BY
select_technique_character = """
SELECT
    name as Character,
    cursed_technique as Technique
FROM
    characters,
    techniques
WHERE
    characters.id = techniques.sorcerer_id
GROUP BY
    characters.name
"""

character_technique = execute_read_query(connection, select_technique_character)
for character in character_technique:
    print(character)

print()

#Составить два запроса, в которых будет вложенный SELECT-запрос (вложение с помощью WHERE.
select_character = "SELECT name, age FROM characters WHERE grade = 'Special';"
grades = execute_read_query(connection, select_character)
for user in grades:
    print(user)

print()

select_domains = "SELECT domain, sorcerer_id FROM domain_expansions WHERE skill = 1000;"
domains = execute_read_query(connection, select_domains)
for domain in domains:
    print(domain)

print()

#составить 2 запроса с использованием UNION (объединение запросов).
union_sorcerer = """
SELECT characters.name, 'sorcerer' as Job FROM characters 
UNION
SELECT clans.head_clan, 'head clan' as HeadClan  FROM clans;
"""
sorcerers = execute_read_query(connection, union_sorcerer)
for sorcerer in sorcerers:
    print(sorcerer)

print()

#Составить 1 запрос с использованием DISTINCT. Если для демонстрации работы этого ключевого
#слова недостаточно данных – предварительно дополните таблицу.
distinct_names = """
SELECT DISTINCT name FROM characters
UNION 
select DISTINCT head_clan FROM clans
"""
names = execute_read_query(connection, distinct_names)
for name in names:
    print(name)

#Обновить две записи в двух разных таблицах Вашей базы данных
update_character_grade = """
UPDATE characters
SET grade = 'God'
WHERE characters.name = 'Satoru';
"""
execute_read_query(connection, update_character_grade)

update_technique = """
UPDATE techniques
SET cursed_technique = 'Syx Eyes and Infinity'
WHERE techniques.sorcerer_id = 1;
"""

execute_read_query(connection, update_technique)

#удалить по одной записи из каждой таблицы.
delete_character = """
DELETE FROM characters
WHERE characters.name = 'Sukuna';
"""
execute_query(connection, delete_character)

delete_technique = """
DELETE FROM techniques
WHERE techniques.cursed_technique = 'Scissors';
"""
execute_query(connection, delete_technique)

delete_clan = """
DELETE FROM clans
WHERE clans.head_clan = 'Kenjaku';
"""
execute_query(connection, delete_clan)

delete_domain = """
DELETE FROM domain_expansions
WHERE sorcerer_id = 6;
"""
execute_query(connection, delete_domain)

delete_full_clans = """
DELETE FROM clans
"""
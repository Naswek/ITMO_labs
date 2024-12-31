from itertools import dropwhile

import mysql.connector
from mysql.connector import Error

def create_connection(host_name, user_name, user_password):
    connection = None
    try:
        connection = mysql.connector.connect(
            host=host_name,
            user=user_name,
            passwd=user_password
        )
        print("Connection to MySQL DB successful")
    except Error as e:
        print(f"The error '{e}' occurred")

    return connection

connection = create_connection("localhost", "Naswek", "GojoSatoru")

def create_database(connection, query):
    cursor = connection.cursor()
    try:
        cursor.execute(query)
        print("Database created successfully")
    except Error as e:
        print(f"The error '{e}' occurred")

def create_connection(host_name, user_name, user_password, db_name):
    connection = None
    try:
        connection = mysql.connector.connect(
            host=host_name,
            user=user_name,
            passwd=user_password,
            database=db_name
        )
        print("Connection to MySQL DB successful")
    except Error as e:
        print(f"The error '{e}' occurred")

    return connection


create_database_query = "CREATE DATABASE Lab4Inf"
create_database(connection, create_database_query)


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

connection = create_connection("localhost", "Naswek", "GojoSatoru", "Lab4Inf")

#drop = """
#DROP DATABASE Lab4Inf"""
#create_database(connection, drop)

#Создайте таблицы с помощью Python для SQLite и MySQL.
create_character_table = """
CREATE TABLE IF NOT EXISTS characters (
  id INT AUTO_INCREMENT,
  name VARCHAR(200) UNIQUE,
  age INT,
  weapon TEXT,
  grade TEXT,
  sorcerer_id INT,
  PRIMARY KEY (id)
) ENGINE = InnoDB
"""

create_technique_table = """
CREATE TABLE IF NOT EXISTS techniques (
  id INT AUTO_INCREMENT,
  cursed_technique TEXT,
  sorcerer_id INT,
  FOREIGN KEY (sorcerer_id) REFERENCES characters(id),
  PRIMARY KEY (id)
) ENGINE = InnoDB  
"""

create_clan_table = """
CREATE TABLE IF NOT EXISTS clans (
  id INT AUTO_INCREMENT,
  sorcerer_clan TEXT,
  member_count INT,
  head_clan VARCHAR(200),
  FOREIGN KEY fk_head_clan (head_clan) REFERENCES characters(name),
  PRIMARY KEY (id)
) ENGINE = InnoDB
"""

create_domain_expansion_table = """
CREATE TABLE IF NOT EXISTS domain_expansions (
  id INT AUTO_INCREMENT,
  domain TEXT,
  skill INT,
  type TEXT,
  sorcerer_id INT,
  FOREIGN KEY fk_sorcerer_id (sorcerer_id) REFERENCES characters(id),
  PRIMARY KEY (id)
) ENGINE = InnoDB
"""


execute_query(connection, create_character_table)
execute_query(connection, create_technique_table)
execute_query(connection, create_clan_table)
execute_query(connection, create_domain_expansion_table)


create_characters  = """
INSERT INTO
  `characters` (`name`, `age`, `weapon`, `grade`, `sorcerer_id`)
VALUES
  ('Satoru', 28, 'Six Eyes', 'Special', 1),
  ('Suguru', 27, 'Curses', 'Special', 2),
  ('Nanami', 27, 'Sword', 'First', 3),
  ('Yuta', 17, 'Katana', 'Special', 4),
  ('Toji', 21, 'Nunchaki', 'Special', 5),
  ('Kaede', 26, 'Blade', 'Special', 6),
  ('Megumi', 16, 'None', 'First', 7),
  ('Mahito', 1, 'None', 'Special', 8),
  ('Kenjaku', 1000, 'None', 'Special', 9),
  ('Inumaki', 16, 'Voice', 'First', 10),
  ('Takako', 29, 'None', 'First', 11);
"""

create_techniques = """
INSERT INTO
    `techniques` (`cursed_technique`, `sorcerer_id`)
VALUES
    ('Purple', 1),
    ('Curses absorption', 2),
    ('Symmetry', 3),
    ('Copying', 4);
"""

create_clans = """
INSERT INTO
    `clans` (`sorcerer_clan`, `member_count`, `head_clan`)
VALUES 
    ('Gojo', 7, 'Satoru'),
    ('Zenin', 25, 'Megumi'),
    ('Kamo', 13, 'Kenjaku'),
    ('Inumaki', 4, 'Inumaki'),
    ('Fujiwara', 16, 'Takako');
"""

create_domain_expansions = """
INSERT INTO
    `domain_expansions` (`domain`, `skill`, `type`, `sorcerer_id`)
VALUES 
    ('Infinite void', 1000, 'impact on perception', 1),
    ('True and mutual love', 687, 'copying techniques', 4),
    ('Shadow Garden of the Chimera', 548, 'i dont remember', 7),
    ('Self-embodiment of the Ideal', 845, 'dont remember this too', 8);
"""

execute_query(connection, create_characters)
execute_query(connection, create_techniques)
execute_query(connection, create_domain_expansions)
execute_query(connection, create_clans)


#Добавьте по одной новой записи в каждую из  таблиц Вашей базы данных.
add_characters = """
INSERT INTO 
    `characters`(`name`, `age`, `weapon`, `grade`, `sorcerer_id`)
VALUES 
    ('Sukuna', 10000, 'None', 'Special', 12);
"""

add_technique = """
INSERT INTO 
    `techniques` (`cursed_technique`, `sorcerer_id`)
VALUES 
    ('Scissors', 12);
"""

add_clan = """
INSERT INTO 
    `clans` (`sorcerer_clan`, `member_count`, `head_clan`)
VALUES
    ('Geto', 100, 'Suguru');
"""

add_expansion = """
INSERT INTO 
    `domain_expansions` (`domain`, `skill`, `type`, `sorcerer_id`)
VALUES
    ('Garden of Edem', 574, 'illusions', 7);
"""

execute_query(connection, add_characters)
execute_query(connection, add_technique)
execute_query(connection, add_clan)
execute_query(connection, add_expansion)

#составить запрос по извлечению данных с использованием WHERE и GROUP BY
select_technique_character = " SELECT sorcerer_id, MAX(skill) FROM domain_expansions WHERE skill > 500 GROUP BY sorcerer_id"

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

delete_technique = """
DELETE FROM techniques
WHERE techniques.cursed_technique = 'Scissors';
"""

execute_query(connection, delete_technique)

delete_character = """
DELETE FROM characters
WHERE characters.name = 'Sukuna';
"""
execute_query(connection, delete_character)

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
execute_query(connection, delete_full_clans)

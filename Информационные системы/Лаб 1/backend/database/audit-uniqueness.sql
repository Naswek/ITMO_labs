-- Read-only audit before enabling application-level uniqueness on an existing database.
-- Reports old rows that need manual review. No UNIQUE constraints are created here.

SELECT 'lab_work.name' AS rule, lower(btrim(name)) AS value,
       count(*) AS copies, array_agg(id ORDER BY id) AS ids
FROM lab_work
GROUP BY lower(btrim(name))
HAVING count(*) > 1;

SELECT 'lab_work.coordinates' AS rule,
       coordinate_x, coordinate_y, count(*) AS copies,
       array_agg(id ORDER BY id) AS ids
FROM lab_work
GROUP BY coordinate_x, coordinate_y
HAVING count(*) > 1;

SELECT 'discipline.name' AS rule, lower(btrim(name)) AS value,
       count(*) AS copies, array_agg(id ORDER BY id) AS ids
FROM discipline
GROUP BY lower(btrim(name))
HAVING count(*) > 1;

SELECT 'lab_work.author_discipline' AS rule,
       author_id, discipline_id, count(*) AS copies,
       array_agg(id ORDER BY id) AS ids
FROM lab_work
WHERE author_id IS NOT NULL AND discipline_id IS NOT NULL
GROUP BY author_id, discipline_id
HAVING count(*) > 1;

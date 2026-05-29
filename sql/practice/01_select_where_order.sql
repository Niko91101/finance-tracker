SELECT *
FROM users;

SELECT id, username
FROM users;

SELECT DISTINCT username
FROM users;

SELECT username AS user_name
FROM users;

SELECT *
FROM users
WHERE id > 10;

SELECT id, username
FROM users
WHERE username = 'Stassss_new';

SELECT *
FROM users
WHERE date IS NULL;

SELECT *
FROM users
WHERE id IN (2, 5, 9, 16);

SELECT *
FROM users
WHERE username LIKE '%new%';

SELECT *
FROM users
ORDER BY id DESC;

SELECT id, username
FROM users
ORDER BY username;

SELECT *
FROM users
LIMIT 5;

SELECT *
FROM users
WHERE date IS NOT NULL
ORDER BY date DESC
LIMIT 3;
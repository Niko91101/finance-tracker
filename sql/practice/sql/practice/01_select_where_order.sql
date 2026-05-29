SELECT user_id, COUNT(amount), SUM(amount)
FROM transactions
GROUP BY user_id
HAVING SUM(amount) > 3000
ORDER BY SUM(amount) DESC;

SELECT user_id, COUNT(amount), SUM(amount)
FROM transactions
GROUP BY user_id
ORDER BY SUM(amount) DESC;

SELECT user_id, COUNT(amount)
FROM transactions
GROUP BY user_id
HAVING COUNT(amount) > 1;

SELECT user_id, SUM(amount)
FROM transactions
GROUP BY user_id;

SELECT user_id, COUNT(*)
FROM transactions
GROUP BY user_id;
INSERT INTO categories (name, type)
VALUES
    ('Еда', 'EXPENSE'),
    ('Транспорт', 'EXPENSE'),
    ('Зарплата', 'INCOME'),
    ('Спортзал', 'EXPENSE'),
    ('Подписки сервисов', 'EXPENSE')
ON CONFLICT DO NOTHING;
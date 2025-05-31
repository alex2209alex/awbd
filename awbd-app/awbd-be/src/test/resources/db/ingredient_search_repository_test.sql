INSERT INTO producers (id, name, address)
VALUES (1, 'Producer 1', 'Producer Address 1'),
       (2, 'Producer 2', 'Producer Address 2');

INSERT INTO ingredients (id, name, price, calories, producer_id)
VALUES (1, 'Ingredient 1', 10, 10, 1),
       (2, 'Ingredient 2', 20, 20, 2);

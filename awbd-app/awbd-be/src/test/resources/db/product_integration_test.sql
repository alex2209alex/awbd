INSERT INTO users (id, email, password, name, role, phone_number, salary)
VALUES (1, 'restaurant@admin.com', 'password', 'Restaurant Admin', 'RESTAURANT_ADMIN', '0712345678', 5000);

INSERT INTO producers (id, name, address)
VALUES (1, 'Producer', 'Producer address');

INSERT INTO ingredients (id, name, price, calories, producer_id)
VALUES (1, 'Ingredient', 1, 10, 1),
       (2, 'Ingredient 2', 1, 10, 1);

INSERT INTO products (id, name, price, description)
VALUES (1, 'Product', 100, 'Product description'),
       (2, 'Product 2', 200, 'Product description 2');

INSERT INTO ingredient_product_associations (ingredient_id, product_id, quantity)
VALUES (1, 1, 100),
       (1, 2, 200);

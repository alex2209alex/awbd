INSERT INTO users (id, email, password, name, role, phone_number, salary)
VALUES (1, 'restaurant@admin.com', 'password', 'Restaurant Admin', 'RESTAURANT_ADMIN', '0712345678', 5000);

INSERT INTO producers (id, name, address)
VALUES (1, 'Producer Name', 'Producer Address'),
       (2, 'Producer Name 2', 'Producer Address 2');

INSERT INTO ingredients (id, name, price, calories, producer_id)
VALUES (1, 'Ingredient Name', 10, 100, 1);

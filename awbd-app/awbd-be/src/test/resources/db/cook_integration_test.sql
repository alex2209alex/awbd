INSERT INTO users (id, email, password, name, role, phone_number, salary)
VALUES (1, 'restaurant@admin.com', 'password', 'Restaurant Admin', 'RESTAURANT_ADMIN', '0712345678', 5000),
       (2, 'cook@cook.com', 'password', 'Cook', 'COOK', null, 2000),
       (3, 'cook@delted.com', 'password', 'Cook For Deletion', 'COOK', null, 2000);

INSERT INTO products (id, name, price, description)
VALUES (1, 'Product', 100, 'Product description'),
       (2, 'Product 2', 200, 'Product description 2');

INSERT INTO cook_product_associations (cook_id, product_id)
VALUES (2, 1);

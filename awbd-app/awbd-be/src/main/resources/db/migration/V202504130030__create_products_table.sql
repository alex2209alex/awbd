CREATE SEQUENCE products_seq INCREMENT BY 20 START WITH 100;

CREATE TABLE products
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(50)    NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    description VARCHAR(100)   NOT NULL
);

CREATE TABLE ingredient_product_associations
(
    ingredient_id BIGINT         NOT NULL REFERENCES ingredients (id),
    product_id    BIGINT         NOT NULL REFERENCES products (id),
    quantity      NUMERIC(10, 2) NOT NULL,
    PRIMARY KEY (ingredient_id, product_id)
);

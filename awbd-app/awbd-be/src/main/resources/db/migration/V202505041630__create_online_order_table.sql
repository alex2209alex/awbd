CREATE SEQUENCE online_orders_seq INCREMENT BY 20 START WITH 100;

CREATE TABLE online_orders
(
    id                  BIGINT PRIMARY KEY,
    address             VARCHAR(100)   NOT NULL,
    price               NUMERIC(10, 2) NOT NULL,
    creation_time       TIMESTAMP      NOT NULL,
    online_order_status VARCHAR(50)    NOT NULL,
    client_id           BIGINT         NOT NULL REFERENCES users (id),
    courier_id          BIGINT REFERENCES users (id)
);

CREATE TABLE product_online_order_associations
(
    product_id      BIGINT NOT NULL REFERENCES products (id),
    online_order_id BIGINT NOT NULL REFERENCES online_orders (id),
    quantity        INT    NOT NULL,
    PRIMARY KEY (product_id, online_order_id)
);

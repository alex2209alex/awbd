CREATE SEQUENCE loyalty_cards_seq INCREMENT BY 20 START WITH 100;

CREATE TABLE loyalty_cards
(
    id     BIGINT PRIMARY KEY,
    points BIGINT
);

CREATE SEQUENCE users_seq INCREMENT BY 20 START WITH 100;

CREATE TABLE users
(
    id              BIGINT PRIMARY KEY,
    email           VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(100) NOT NULL,
    name            VARCHAR(50)  NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    phone_number    VARCHAR(10),
    salary          NUMERIC(5, 2),
    loyalty_card_id BIGINT REFERENCES loyalty_cards (id)
);

CREATE TABLE cook_product_associations
(
    cook_id    BIGINT NOT NULL REFERENCES users (id),
    product_id BIGINT NOT NULL REFERENCES products (id),
    PRIMARY KEY (cook_id, product_id)
);

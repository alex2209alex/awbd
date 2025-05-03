CREATE SEQUENCE ingredients_seq INCREMENT BY 20 START WITH 100;

CREATE TABLE ingredients
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(50)   NOT NULL,
    price       NUMERIC(5, 2) NOT NULL,
    calories    NUMERIC(5, 2) NOT NULL,
    producer_id BIGINT        NOT NULL REFERENCES producers (id)
);
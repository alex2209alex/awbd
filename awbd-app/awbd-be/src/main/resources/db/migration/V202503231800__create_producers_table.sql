CREATE SEQUENCE producers_seq INCREMENT BY 20 START WITH 100;

CREATE TABLE producers
(
    id      BIGINT PRIMARY KEY,
    name    VARCHAR(50)  NOT NULL,
    address VARCHAR(100) NOT NULL
);

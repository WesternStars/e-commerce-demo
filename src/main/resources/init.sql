DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

CREATE TABLE product
(
    sku   varchar(15) PRIMARY KEY,
    name  varchar(40) NOT NULL,
    price real        NOT NULL
);

CREATE TABLE category
(
    id   varchar(15) PRIMARY KEY,
    name varchar(40) NOT NULL
);

INSERT INTO product (sku, name, price)
VALUES ('ABC0000', 'Hammer', 1.0);
INSERT INTO product (sku, name, price)
VALUES ('ABC0001', 'Screwdriver', 1.1);
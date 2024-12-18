DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id   integer PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE product
(
    sku        varchar(15) PRIMARY KEY,
    name       varchar(40) NOT NULL,
    price      real        NOT NULL,
    categoryId integer     NOT NULL REFERENCES category
);

INSERT INTO category (id, name)
VALUES (1, 'Tools');

INSERT INTO product (sku, name, price, categoryId)
VALUES ('ABC0000', 'Hammer', 1.0, 1);
INSERT INTO product (sku, name, price, categoryId)
VALUES ('ABC0001', 'Screwdriver', 1.1, 1);
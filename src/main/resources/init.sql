DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id   serial PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE product
(
    sku        varchar(15) PRIMARY KEY,
    name       varchar(40) NOT NULL,
    price      real        NOT NULL,
    category_id integer     NOT NULL REFERENCES category
);

INSERT INTO category (name)
VALUES ('Tools');

INSERT INTO product (sku, name, price, category_id)
VALUES ('ABC0000', 'Hammer', 1.0, 1),
       ('ABC0001', 'Screwdriver', 1.1, 1);
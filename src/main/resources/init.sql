DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS e_order;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id   serial PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE product
(
    sku         varchar(15) PRIMARY KEY,
    name        varchar(40) NOT NULL,
    price       real        NOT NULL,
    category_id integer     NOT NULL REFERENCES category
);

CREATE TABLE e_order
(
    id           serial PRIMARY KEY,
    total_amount integer NOT NULL
);

CREATE TABLE order_item
(
    id          serial PRIMARY KEY,
    quantity    integer     NOT NULL,
    product_sku varchar(15) NOT NULL REFERENCES product,
    order_id    integer REFERENCES e_order
);

INSERT INTO category (name)
VALUES ('Tools');

INSERT INTO product (sku, name, price, category_id)
VALUES ('ABC0000', 'Hammer', 1.0, 1),
       ('ABC0001', 'Screwdriver', 1.1, 1);
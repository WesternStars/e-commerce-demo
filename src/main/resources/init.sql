DROP TABLE IF EXISTS order_link;
DROP TABLE IF EXISTS e_order;
DROP TABLE IF EXISTS order_item;
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

CREATE TABLE order_item
(
    id         serial PRIMARY KEY,
    quantity   integer NOT NULL,
    product_id integer NOT NULL REFERENCES product
);

CREATE TABLE order
(
    id           serial PRIMARY KEY,
    total_amount integer NOT NULL
);

CREATE TABLE order_link
(
    order_id      integer NOT NULL REFERENCES e_order,
    order_item_id integer NOT NULL REFERENCES order_item
);

INSERT INTO category (name)
VALUES ('Tools');

INSERT INTO product (sku, name, price, category_id)
VALUES ('ABC0000', 'Hammer', 1.0, 1),
       ('ABC0001', 'Screwdriver', 1.1, 1);
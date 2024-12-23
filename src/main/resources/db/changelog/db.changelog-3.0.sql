--changeset apuhachov:1
INSERT INTO category (name)
VALUES ('Tools');

INSERT INTO product (sku, name, price, category_id)
VALUES ('ABC0000', 'Hammer', 12.0, 1),
       ('ABC0001', 'Screwdriver', 5.5, 1),
       ('ABC0100', 'Hammer and Screwdriver', 11, 1),
       ('ABC0011', 'Channellock Adjustable Wrench', 12, 1),
       ('ABC0101', 'AmazonBasics Set of 4 Pliers', 15, 1),
       ('ABC1001', 'Orbital Sander', 31, 1),
       ('ABC1000', 'DEWALT 20-Volt Brushless Compact', 25, 1);

INSERT INTO e_order (total_amount)
VALUES (190.0),
       (24.0),
       (111.0);

INSERT INTO order_item (quantity, product_sku, order_id)
VALUES (2, 'ABC0100', 1),
       (4, 'ABC0011', 1),
       (8, 'ABC0101', 1);

INSERT INTO order_item (quantity, product_sku, order_id)
VALUES (2, 'ABC0000', 2);

INSERT INTO order_item (quantity, product_sku, order_id)
VALUES (2, 'ABC0001', 3),
       (4, 'ABC1000', 3);
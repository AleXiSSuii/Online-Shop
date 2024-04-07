INSERT INTO product (name,description,price,quantity)
VALUES ('Продукт1','Описание1',100.0,5);
INSERT INTO product (name,description,price,quantity)
VALUES ('Продукт2','Описание2',200.0,10);

INSERT INTO cart (final_price)
VALUES (900);

INSERT INTO cart_item (quantity,price,product_id,cart_id)
VALUES (3,300,1,1);
INSERT INTO cart_item (quantity,price,product_id,cart_id)
VALUES (3,600,2,1);


INSERT INTO address (state, city, street, building, apartment, postal_code)
VALUES ('Moscow', 'Moscow','Arbat',10,10,'111111');

INSERT INTO users (email,number, lastname,name, password,role, status,cart_id,address_id)
VALUES ('user@mail.com', '12345678', 'User','User','$2a$12$IEPEsP2UdHswCCSD1K9nJ.bp1ZZ6R.5fbCLOXvwrjHYuSlT4agsoK', 'USER','ACTIVE',1,1);

INSERT INTO orders(total_price, order_date, user_id, status)
VALUES (900, '2024-01-01 10:00:00', 1, 'HANDLING');

INSERT INTO cart_item (quantity, price, product_id, order_id)
VALUES (3, 300, 1, 1);
INSERT INTO cart_item (quantity,price,product_id,order_id)
VALUES (3,600,2,1);


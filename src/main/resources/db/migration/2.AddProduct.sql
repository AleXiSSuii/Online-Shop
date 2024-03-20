
--add products
INSERT INTO product(id,name,description,price,category_id,quantity)
VALUES (1, 'Продукт1','Описание1',100,10,1,3);
INSERT INTO product(id,name,description,price,category_id,quantity)
VALUES (2, 'Продукт2','Описание2',500,2,10);
INSERT INTO product(id,name,description,price,category_id)
VALUES (3, 'Продукт3','Описание3',1000,2);
INSERT INTO category(id,name)
VALUES (1, 'Категория 1');
INSERT INTO category(id,name)
VALUES (2, 'Категория 2');

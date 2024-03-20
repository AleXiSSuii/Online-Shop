
--admin Password: admin
insert into users (email, number,lastname,name ,password, role,status)
values (1,'admin@mail.com', 000000,'admin','admin','$2y$12$xkiMqoqksiS2VnFW7D8VU.5ez05LcWj0loiD1vpfsl0SVF3yhU4u2', 'ADMIN','ACTIVE');
--user1 Password: user
insert into users (email, number,lastname,name ,password, role,status,address_id)
values (2,'user@mail.com', 777777777,'user','user','$2y$12$iYBpTyPZWEM4rbmiYDClzOUAioqTlh7M2c4x4yj0BIyLarGNrkwta', 'USER','ACTIVE',1,1);
--user2 Password: user
insert into users (email, number,lastname,name ,password, role,status)
values (2,'user@mail.com', 777777777,'user','user','$2y$12$iYBpTyPZWEM4rbmiYDClzOUAioqTlh7M2c4x4yj0BIyLarGNrkwta', 'USER','ACTIVE',2);
--cart for user1
insert into cart(id,final_price)
values (1,0);
--cart for user2
insert into cart(id,final_price)
values (2,0);
--address for user1
insert into users (state,city, street,building,apartment ,postal_code)
values ('Moscow','Moscow', 'Lenina','user','1','1', '111111');

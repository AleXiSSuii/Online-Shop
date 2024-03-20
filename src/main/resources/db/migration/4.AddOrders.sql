--Orders for user1

INSERT INTO orders (id,total_price,order_date,status,user_id)
VALUES (1,1500,CURRENT_TIMESTAMP,'HANDLING',2);
INSERT INTO orders (id,total_price,order_date,status,user_id)
VALUES (2,500,CURRENT_TIMESTAMP,'CLOSED',2);
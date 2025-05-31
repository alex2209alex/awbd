INSERT INTO users (id, email, password, name, role, phone_number, salary, loyalty_card_id)
VALUES (1, 'restaurant@admin.com', '123456', 'Restaurant Admin', 'RESTAURANT_ADMIN', '0700000000', 5000, null),
       (2, 'client@client.com', '123456', 'Client 1', 'CLIENT', '0711111111', null, null),
       (3, 'courier@courier.com', '123456', 'Client 1', 'COURIER', '0722222222', 2000, null),
       (4, 'courier2@courier2.com', '123456', 'Courier 2', 'COURIER', '0733333333', 2500, null),
       (5, 'cook@cook.com', '123456', 'Cook 2', 'COOK', '0744444444', 2500, null),
       (6, 'client2@client2.com', '123456', 'Client 2', 'CLIENT', '0755555555', null, null);

INSERT INTO online_orders (id, address, price, creation_time, online_order_status, client_id, courier_id)
VALUES (1, 'OnlineOrder 1 address', 100, now(), 'ON_DELIVERY', 2, 3),
       (2, 'OnlineOrder 2 address', 200, now(), 'IN_PREPARATION', 6, null),
       (3, 'OnlineOrder 3 address', 300, now(), 'READY', 6, null);

INSERT INTO users (id, email, password, name, role, phone_number, salary)
VALUES (1, 'restaurant@admin.com', 'password', 'Restaurant Admin', 'RESTAURANT_ADMIN', '0712345678', 5000),
       (2, 'courier@courier.com', 'password', 'Courier', 'COURIER', '0712345678', 2000),
       (3, 'courier@delted.com', 'password', 'Courier For Deletion', 'COURIER', '0787654321', 2000),
       (4, 'client@client.com', 'password', 'Client', 'CLIENT', '0798765432', null);

INSERT INTO online_orders (id, address, price, creation_time, online_order_status, client_id, courier_id)
VALUES (1, 'Address', 100, now(), 'DELIVERED', 4, 2);

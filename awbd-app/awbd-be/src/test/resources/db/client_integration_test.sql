INSERT INTO loyalty_cards (id, points)
VALUES (1, 100);

INSERT INTO users (id, email, password, name, role, phone_number, loyalty_card_id)
VALUES (1, 'client@client.com', 'password', 'Client Client', 'CLIENT', '0712345678', 1);
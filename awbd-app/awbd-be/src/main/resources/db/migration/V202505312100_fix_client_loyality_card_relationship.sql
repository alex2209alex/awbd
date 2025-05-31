ALTER TABLE users
    DROP COLUMN loyalty_card_id;

DELETE FROM loyalty_cards;

DROP SEQUENCE loyalty_cards_seq;

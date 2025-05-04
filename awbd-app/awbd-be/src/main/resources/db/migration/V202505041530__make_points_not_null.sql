UPDATE loyalty_cards
SET points = 0
WHERE points IS NULL;

ALTER TABLE loyalty_cards
    ALTER COLUMN points SET NOT NULL;

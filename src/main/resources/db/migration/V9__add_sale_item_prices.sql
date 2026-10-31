ALTER TABLE sale_items
    ADD COLUMN unit_price DECIMAL(19, 2),
    ADD COLUMN additional_amount DECIMAL(19, 2) NOT NULL DEFAULT 0.00;

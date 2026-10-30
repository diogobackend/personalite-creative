ALTER TABLE sales
    ALTER COLUMN delivery_deadline DROP NOT NULL;
    ADD COLUMN observation TEXT;

ALTER TABLE sale_items
    ADD COLUMN quantity INTEGER NOT NULL DEFAULT 1,
    ADD COLUMN color VARCHAR(100),
    ADD COLUMN size VARCHAR(50),
    ADD COLUMN material VARCHAR(100),
    ADD COLUMN brand VARCHAR(100),
    ADD COLUMN model VARCHAR(100);

ALTER TABLE sale_items
    ADD CONSTRAINT chk_sale_items_quantity
    CHECK (quantity > 0);
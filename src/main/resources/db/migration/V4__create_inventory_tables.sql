-- PRODUCTS
ALTER TABLE products
    ALTER COLUMN category TYPE VARCHAR(150),
    ADD COLUMN category_id UUID;

ALTER TABLE products
    ADD CONSTRAINT fk_products_category
    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE SET NULL;

CREATE INDEX idx_products_category_id
    ON products(category_id);


-- INVENTORY
ALTER TABLE inventory
    ALTER COLUMN category TYPE VARCHAR(150),
    ADD COLUMN category_id UUID;

ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_category
    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE SET NULL;

CREATE INDEX idx_inventory_category_id
    ON inventory(category_id);


-- EXPENSES
ALTER TABLE expenses
    ALTER COLUMN category TYPE VARCHAR(150),
    ADD COLUMN category_id UUID;

ALTER TABLE expenses
    ADD CONSTRAINT fk_expenses_category
    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE SET NULL;

CREATE INDEX idx_expenses_category_id
    ON expenses(category_id);

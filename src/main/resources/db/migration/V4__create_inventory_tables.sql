CREATE TABLE inventory (
    inventory_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    description TEXT,
    product_id UUID,
    quantity INTEGER NOT NULL DEFAULT 0,
    minimum_quantity INTEGER,
    unit VARCHAR(255),
    color VARCHAR(255),
    size VARCHAR(255),
    material VARCHAR(255),
    brand VARCHAR(255),
    model VARCHAR(255),
    sku VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_inventory_sku UNIQUE (sku),
    CONSTRAINT ck_inventory_quantity CHECK (quantity >= 0),
    CONSTRAINT ck_inventory_minimum_quantity
        CHECK (minimum_quantity IS NULL OR minimum_quantity >= 0)
);

CREATE TABLE inventory_movements (
    movement_id UUID PRIMARY KEY,
    inventory_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    reason VARCHAR(255) NOT NULL,
    expense_id UUID,
    sale_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_inventory_movements_inventory
        FOREIGN KEY (inventory_id)
        REFERENCES inventory(inventory_id),

    CONSTRAINT ck_inventory_movements_quantity
        CHECK (quantity > 0),

    CONSTRAINT ck_inventory_movements_type
        CHECK (type IN ('IN', 'OUT', 'ADJUSTMENT'))
);

CREATE INDEX idx_inventory_product_id
    ON inventory(product_id);

CREATE INDEX idx_inventory_movements_inventory_id
    ON inventory_movements(inventory_id);
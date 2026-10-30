CREATE TABLE sales (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    delivery_deadline DATE NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    down_payment_percentage NUMERIC(5, 2) NOT NULL,
    paid_percentage NUMERIC(5, 2) NOT NULL DEFAULT 0,
    payment_method VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sales_customer
        FOREIGN KEY (customer_id)
        REFERENCES users(user_id),

    CONSTRAINT chk_sales_total_amount
        CHECK (total_amount > 0),

    CONSTRAINT chk_sales_down_payment_percentage
        CHECK (down_payment_percentage BETWEEN 0 AND 100),

    CONSTRAINT chk_sales_paid_percentage
        CHECK (paid_percentage BETWEEN 0 AND 100),

    CONSTRAINT chk_sales_payment_method
        CHECK (
            payment_method IN (
                'PIX',
                'CASH',
                'CREDIT_CARD',
                'DEBIT_CARD',
                'BANK_TRANSFER',
                'BANK_SLIP'
            )
        )
);

CREATE TABLE sale_items (
    id UUID PRIMARY KEY,
    sale_id UUID NOT NULL,
    product_id UUID NOT NULL,
    inventory_id UUID,

    CONSTRAINT fk_sale_items_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(id),

    CONSTRAINT fk_sale_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_sale_items_inventory
        FOREIGN KEY (inventory_id)
        REFERENCES inventory(inventory_id)
);

CREATE INDEX idx_sales_customer_id
    ON sales(customer_id);

CREATE INDEX idx_sale_items_sale_id
    ON sale_items(sale_id);

CREATE INDEX idx_sale_items_product_id
    ON sale_items(product_id);

CREATE INDEX idx_sale_items_inventory_id
    ON sale_items(inventory_id);
CREATE TABLE expenses (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    value NUMERIC(15, 2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    allocation_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    quantity INTEGER,
    expense_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_expenses_value
        CHECK (value > 0),

    CONSTRAINT chk_expenses_quantity
        CHECK (
            (category = 'STOCK' AND quantity > 0)
            OR
            (category <> 'STOCK' AND quantity IS NULL)
        )
);

CREATE TABLE expense_products (
    id UUID PRIMARY KEY,
    expense_id UUID NOT NULL,
    product_id UUID NOT NULL,
    allocated_value NUMERIC(15, 2) NOT NULL,

    CONSTRAINT fk_expense_products_expense
        FOREIGN KEY (expense_id)
        REFERENCES expenses(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_expense_products
        UNIQUE (expense_id, product_id),

    CONSTRAINT chk_expense_products_allocated_value
        CHECK (allocated_value > 0)
);

CREATE INDEX idx_expenses_category ON expenses(category);
CREATE INDEX idx_expenses_expense_date ON expenses(expense_date);
CREATE INDEX idx_expense_products_product_id ON expense_products(product_id);
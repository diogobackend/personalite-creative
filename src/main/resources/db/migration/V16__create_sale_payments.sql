CREATE TABLE sale_payments (
    id UUID PRIMARY KEY,
    sale_id UUID NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    paid_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_sale_payments_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(id),

    CONSTRAINT chk_sale_payments_amount
        CHECK (amount > 0),

    CONSTRAINT chk_sale_payments_type
        CHECK (type IN ('RECEIPT', 'REFUND'))
);

CREATE INDEX idx_sale_payments_sale_id
    ON sale_payments (sale_id);

CREATE INDEX idx_sale_payments_paid_at
    ON sale_payments (paid_at);
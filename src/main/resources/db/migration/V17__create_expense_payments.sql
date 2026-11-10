CREATE TABLE expense_payments (
    id UUID PRIMARY KEY,
    expense_id UUID NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    paid_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_expense_payments_expense
        FOREIGN KEY (expense_id) REFERENCES expenses(id),

    CONSTRAINT chk_expense_payments_amount
        CHECK (amount > 0),

    CONSTRAINT chk_expense_payments_type
        CHECK (type IN ('PAYMENT', 'REFUND'))
);

CREATE INDEX idx_expense_payments_expense_id
    ON expense_payments (expense_id);

CREATE INDEX idx_expense_payments_paid_at
    ON expense_payments (paid_at);
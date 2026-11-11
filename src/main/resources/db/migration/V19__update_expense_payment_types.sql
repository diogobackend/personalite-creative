ALTER TABLE expense_payments
    ALTER COLUMN type TYPE VARCHAR(40);

ALTER TABLE expense_payments
    DROP CONSTRAINT chk_expense_payments_type;

ALTER TABLE expense_payments
    ADD CONSTRAINT chk_expense_payments_type
    CHECK (
        type IN (
            'PAYMENT',
            'REFUND',
            'VALUE_ADJUSTMENT_INCREASE',
            'VALUE_ADJUSTMENT_DECREASE',
            'CANCELLED'
        )
    );

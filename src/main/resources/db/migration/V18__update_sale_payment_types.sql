-- Atualizar tipos de movimentação financeira
ALTER TABLE sale_payments
ALTER COLUMN type TYPE VARCHAR(30);

ALTER TABLE sale_payments
DROP CONSTRAINT IF EXISTS chk_sale_payments_type;

ALTER TABLE sale_payments
ADD CONSTRAINT chk_sale_payments_type
CHECK (
    type IN (
        'RECEIPT',
        'REFUND',
        'VALUE_ADJUSTMENT_INCREASE',
        'VALUE_ADJUSTMENT_DECREASE',
        'CANCELLED'
    )
);

ALTER TABLE sale_payments
DROP CONSTRAINT IF EXISTS chk_sale_payments_amount;

ALTER TABLE sale_payments
ADD CONSTRAINT chk_sale_payments_amount
CHECK (
    amount > 0
    OR (type = 'CANCELLED' AND amount = 0)
);

-- Persistir o valor efetivamente recebido
ALTER TABLE sales
ADD COLUMN received_amount NUMERIC(12, 2);

-- Preservar os valores recebidos das vendas existentes
UPDATE sales
SET received_amount = ROUND(
    total_amount * paid_percentage / 100,
    2
);

-- Garantir integridade financeira
ALTER TABLE sales
ALTER COLUMN received_amount SET NOT NULL;

ALTER TABLE sales
ADD CONSTRAINT chk_sales_received_amount
CHECK (
    received_amount >= 0
    AND received_amount <= total_amount
);

-- Sincronizar o percentual armazenado com o valor recebido
UPDATE sales
SET paid_percentage = ROUND(
    received_amount * 100 / total_amount,
    2
);
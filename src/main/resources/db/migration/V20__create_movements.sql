CREATE TABLE movements (
    id UUID PRIMARY KEY,
    origin_id UUID NOT NULL,
    origin VARCHAR(20) NOT NULL,
    operation VARCHAR(40) NOT NULL,
    direction VARCHAR(10),
    status VARCHAR(40),
    previous_value NUMERIC(19, 2),
    current_value NUMERIC(19, 2),
    amount NUMERIC(19, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_movements_origin
        CHECK (origin IN ('SALE', 'EXPENSE')),

    CONSTRAINT chk_movements_operation
        CHECK (operation IN (
            'CREATED',
            'UPDATED',
            'PAYMENT',
            'REFUND',
            'VALUE_ADJUSTMENT_INCREASE',
            'VALUE_ADJUSTMENT_DECREASE',
            'CANCELLED'
        )),

    CONSTRAINT chk_movements_direction
        CHECK (direction IS NULL OR direction IN ('IN', 'OUT')),

    CONSTRAINT chk_movements_amount
        CHECK (amount >= 0)
);

CREATE INDEX idx_movements_created_at
    ON movements (created_at);

CREATE INDEX idx_movements_origin
    ON movements (origin, origin_id);

CREATE INDEX idx_movements_status
    ON movements (status);

CREATE INDEX idx_movements_direction
    ON movements (direction);

CREATE INDEX idx_movements_operation
    ON movements (operation);

CREATE INDEX idx_movements_amount
    ON movements (amount);

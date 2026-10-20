
CREATE TABLE products (
    id UUID PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(30) NOT NULL,
    description TEXT,
    minimum_quantity INTEGER,
    pricing_type VARCHAR(20) NOT NULL,
    shelf_life_days INTEGER,
    production_lead_time_min_days INTEGER,
    production_lead_time_max_days INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_products_code
        CHECK (code ~ '^[0-9]{3}$'),

    CONSTRAINT chk_products_minimum_quantity
        CHECK (minimum_quantity > 0),

    CONSTRAINT chk_products_pricing_type
        CHECK (pricing_type IN ('FIXED', 'TIERED', 'QUOTE')),

    CONSTRAINT chk_products_lead_time
        CHECK (
            production_lead_time_min_days IS NULL
            OR production_lead_time_max_days IS NULL
            OR production_lead_time_max_days >= production_lead_time_min_days
        )
);

CREATE TABLE product_prices (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    minimum_quantity INTEGER NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_prices_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT uk_product_prices_quantity
        UNIQUE (product_id, minimum_quantity),

    CONSTRAINT chk_product_prices_quantity
        CHECK (minimum_quantity > 0),

    CONSTRAINT chk_product_prices_unit_price
        CHECK (unit_price > 0)
);

CREATE TABLE product_fees (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    charge_type VARCHAR(30) NOT NULL,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_fees_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT chk_product_fees_amount
        CHECK (amount >= 0),

    CONSTRAINT chk_product_fees_charge_type
        CHECK (
            charge_type IN (
                'PER_ORDER',
                'PER_UNIT',
                'FIRST_PURCHASE'
            )
        )
);

CREATE INDEX idx_product_prices_product_id
    ON product_prices(product_id);

CREATE INDEX idx_product_fees_product_id
    ON product_fees(product_id);

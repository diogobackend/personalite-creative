CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    type VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_categories_name_type
        UNIQUE (name, type),

    CONSTRAINT ck_categories_type
        CHECK (type IN ('PRODUCT', 'INVENTORY', 'EXPENSE'))
);
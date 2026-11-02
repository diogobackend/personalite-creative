CREATE TABLE product_colors (
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    color VARCHAR(100) NOT NULL,
    CONSTRAINT uk_product_colors UNIQUE (product_id, color)
);

CREATE TABLE product_sizes (
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    size VARCHAR(100) NOT NULL,
    CONSTRAINT uk_product_sizes UNIQUE (product_id, size)
);

CREATE TABLE product_brands (
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    brand VARCHAR(100) NOT NULL,
    CONSTRAINT uk_product_brands UNIQUE (product_id, brand)
);

CREATE TABLE product_models (
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    model VARCHAR(100) NOT NULL,
    CONSTRAINT uk_product_models UNIQUE (product_id, model)
);

CREATE TABLE product_materials (
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    material VARCHAR(100) NOT NULL,
    CONSTRAINT uk_product_materials UNIQUE (product_id, material)
);
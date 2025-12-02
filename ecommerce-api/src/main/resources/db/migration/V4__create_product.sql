CREATE TABLE product (

    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255) NOT NULL,
    measure_unit VARCHAR(255) NOT NULL,
    available_quantity NUMERIC(19, 3) NOT NULL,
    price NUMERIC(38, 2) NOT NULL,

    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_by VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
);
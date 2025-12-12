CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description VARCHAR(255) NOT NULL,
                         measure_unit VARCHAR(50) NOT NULL,
                         available_quantity NUMERIC(19, 3) NOT NULL,
                         price NUMERIC(19, 2) NOT NULL,
                         active BOOLEAN NOT NULL DEFAULT TRUE,

                         category_id INTEGER NOT NULL,

                         created_by VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         last_modified_by VARCHAR(255),
                         updated_at TIMESTAMP,

                         CONSTRAINT uq_product_name UNIQUE (name),
                         CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id)
);
CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) UNIQUE NOT NULL,
                         description VARCHAR(255) NOT NULL,
                         available_quantity INTEGER NOT NULL,
                         price NUMERIC(19, 2) NOT NULL,


                         category_id INTEGER NOT NULL,


                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITHOUT TIME ZONE,

                         CONSTRAINT fk_product_category
                             FOREIGN KEY (category_id)
                                 REFERENCES category (id)
);
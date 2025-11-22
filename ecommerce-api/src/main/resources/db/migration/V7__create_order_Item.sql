CREATE TABLE order_item (
                            id BIGSERIAL PRIMARY KEY,
                            quantity INTEGER NOT NULL,
                            unit_price NUMERIC(19, 2) NOT NULL,


                            product_id BIGINT NOT NULL,
                            order_id UUID NOT NULL,

                            created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                            updated_at TIMESTAMP WITHOUT TIME ZONE,

                            CONSTRAINT fk_order_item_product
                                FOREIGN KEY (product_id)
                                    REFERENCES product (id),

                            CONSTRAINT fk_order_item_order
                                FOREIGN KEY (order_id)
                                    REFERENCES orders (id)
);
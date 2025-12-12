CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        reference VARCHAR(255) NOT NULL,
                        total_amount NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
                        order_status VARCHAR(50) NOT NULL,

                        user_id UUID NOT NULL,
                        payment_id UUID,

                        created_by VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        last_modified_by VARCHAR(255),
                        updated_at TIMESTAMP,

                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES user_(id),
                        CONSTRAINT fk_orders_payment FOREIGN KEY (payment_id) REFERENCES payment(id)
);

CREATE INDEX idx_order_user_id ON orders (user_id);
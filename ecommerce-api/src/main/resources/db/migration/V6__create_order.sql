CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        reference VARCHAR(255) NOT NULL,


                        user_id UUID NOT NULL,


                        payment_id UUID UNIQUE,


                        created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                        updated_at TIMESTAMP WITHOUT TIME ZONE,

                        CONSTRAINT fk_order_user
                            FOREIGN KEY (user_id)
                                REFERENCES user_ (id),

                        CONSTRAINT fk_order_payment
                            FOREIGN KEY (payment_id)
                                REFERENCES payment (id)
);
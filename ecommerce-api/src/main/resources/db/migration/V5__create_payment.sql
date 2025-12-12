CREATE TABLE payment (
                         id UUID PRIMARY KEY,
                         reference VARCHAR(255) UNIQUE NOT NULL,
                         amount NUMERIC(19, 2) NOT NULL,


                         status VARCHAR(50) NOT NULL,
                         payment_method VARCHAR(50) NOT NULL,

                         confirmed_at TIMESTAMP WITHOUT TIME ZONE,
                         failure_reason VARCHAR(500),


                         created_by VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         last_modified_by VARCHAR(255),
                         updated_at TIMESTAMP WITHOUT TIME ZONE

                        CONSTRAINT pk_payments PRIMARY KEY (id),
                        CONSTRAINT uk_payments_reference UNIQUE (reference)
);
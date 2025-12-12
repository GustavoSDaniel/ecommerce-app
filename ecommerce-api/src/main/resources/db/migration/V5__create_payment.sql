CREATE TABLE payment (
                         id UUID PRIMARY KEY,
                         reference VARCHAR(255) NOT NULL,
                         amount NUMERIC(19, 2) NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         payment_method VARCHAR(50) NOT NULL,
                         confirmed_at TIMESTAMP,
                         failure_reason VARCHAR(500),

                         created_by VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         last_modified_by VARCHAR(255),
                         updated_at TIMESTAMP,

                         CONSTRAINT uq_payment_reference UNIQUE (reference)
);
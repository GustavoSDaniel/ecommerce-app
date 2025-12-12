CREATE TABLE address (
                         id BIGSERIAL PRIMARY KEY,
                         house_number VARCHAR(255) NOT NULL,
                         complement VARCHAR(255),
                         zip_code VARCHAR(255) NOT NULL,
                         street VARCHAR(255) NOT NULL,
                         bairro VARCHAR(255) NOT NULL,
                         city VARCHAR(255) NOT NULL,
                         state VARCHAR(255) NOT NULL,
                         country VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP,

                         user_id UUID NOT NULL,

                         CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES user_(id) ON DELETE CASCADE
);
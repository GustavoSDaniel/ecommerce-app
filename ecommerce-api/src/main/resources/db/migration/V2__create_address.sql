CREATE TABLE address (
                         id BIGSERIAL PRIMARY KEY,
                         zip_code VARCHAR(20) NOT NULL,
                         house_number VARCHAR(20) NOT NULL,
                         complement VARCHAR(255),
                         street VARCHAR(255) NOT NULL,
                         bairro VARCHAR(255) NOT NULL,
                         city VARCHAR(255) NOT NULL,
                         state VARCHAR(255) NOT NULL,
                         country VARCHAR(255) NOT NULL,

                         user_id UUID NOT NULL,


                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITHOUT TIME ZONE,

                         CONSTRAINT fk_address_user
                             FOREIGN KEY (user_id)
                                 REFERENCES user_ (id)
);
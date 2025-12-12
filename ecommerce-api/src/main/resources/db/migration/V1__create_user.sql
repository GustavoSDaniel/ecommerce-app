CREATE TABLE user_ (
                       id UUID PRIMARY KEY,
                       user_name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       user_role VARCHAR(50) NOT NULL,
                       cpf VARCHAR(255),
                       phone_number VARCHAR(255),

                       created_by VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       last_modified_by VARCHAR(255),
                       updated_at TIMESTAMP,

                       CONSTRAINT uq_user_email UNIQUE (email),
                       CONSTRAINT uq_user_cpf UNIQUE (cpf)
);

CREATE INDEX idx_user_role ON user_(user_role);
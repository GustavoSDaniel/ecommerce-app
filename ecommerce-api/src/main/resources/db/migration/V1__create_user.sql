CREATE TABLE user_ (
                       id UUID PRIMARY KEY,


                       user_name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,


                       email VARCHAR(255) UNIQUE NOT NULL,
                       user_role VARCHAR(50) NOT NULL,
                       cpf VARCHAR(14) UNIQUE,
                       phone_number VARCHAR(20),


                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITHOUT TIME ZONE

);

CREATE INDEX idx_user_role ON user_(user_role);
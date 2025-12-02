CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) UNIQUE NOT NULL,
                          description VARCHAR(255) NOT NULL,
                          created_by  VARCHAR(255) NOT NULL,
                          last_modified_by VARCHAR(255) NULL,
                          created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITHOUT TIME ZONE
);
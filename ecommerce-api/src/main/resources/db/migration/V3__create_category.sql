CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255) NOT NULL,

                          created_by VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          last_modified_by VARCHAR(255),
                          updated_at TIMESTAMP,

                          CONSTRAINT uq_category_name UNIQUE (name)
);
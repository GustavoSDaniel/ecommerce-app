CREATE TABLE notification (
                              id UUID PRIMARY KEY,
                              sender VARCHAR(255) NOT NULL,
                              recipient VARCHAR(255) NOT NULL,
                              content TEXT NOT NULL,
                              sent_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,


                              order_id UUID NOT NULL,
                              payment_id UUID,


                              created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                              updated_at TIMESTAMP WITHOUT TIME ZONE,

                              CONSTRAINT fk_notification_order
                                  FOREIGN KEY (order_id)
                                      REFERENCES orders (id),

                              CONSTRAINT fk_notification_payment
                                  FOREIGN KEY (payment_id)
                                      REFERENCES payment (id)
);
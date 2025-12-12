CREATE TABLE tb_notifications (
                                  id UUID PRIMARY KEY,
                                  recipient VARCHAR(255) NOT NULL,
                                  subject VARCHAR(255) NOT NULL,
                                  status VARCHAR(50) NOT NULL,
                                  content TEXT NOT NULL,
                                  sent_at TIMESTAMP,
                                  failure_reason VARCHAR(500),

                                  user_id UUID,
                                  order_id UUID,
                                  payment_id UUID,

                                  created_by VARCHAR(255) NOT NULL,
                                  created_at TIMESTAMP NOT NULL,
                                  last_modified_by VARCHAR(255),
                                  updated_at TIMESTAMP,

                                  CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES user_(id),
                                  CONSTRAINT fk_notifications_order FOREIGN KEY (order_id) REFERENCES orders(id),
                                  CONSTRAINT fk_notifications_payment FOREIGN KEY (payment_id) REFERENCES payment(id)
);


CREATE TABLE users (
                       id            BIGSERIAL PRIMARY KEY,
                       username      VARCHAR(50)  NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE messages (
                          id          BIGSERIAL PRIMARY KEY,
                          sender_id   BIGINT       NOT NULL REFERENCES users(id),
                          receiver_id BIGINT       NOT NULL REFERENCES users(id),
                          content     TEXT         NOT NULL,
                          sent_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          status      VARCHAR(10)  NOT NULL DEFAULT 'SENT'
                              CHECK (status IN ('SENT', 'DELIVERED', 'READ'))
);

CREATE INDEX idx_messages_conversation
    ON messages (sender_id, receiver_id, sent_at);
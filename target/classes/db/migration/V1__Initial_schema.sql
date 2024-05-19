CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

INSERT INTO users (id, email, password, role) VALUES
(1, 'admin@gmail.com', '$2a$12$QoLS9iyjmM8mj88NQML7/ODm.O3HDusOaMXt4DJzqfaNlZoyfjR8S', 'ADMIN'),
(2, 'userEmail@gmail.com', '$2a$12$8bxW.ABpzaEMuNziJeSQq.xbKvxcmq0yMdYaoWRDFE4R0B6YEuZ1S', 'USER');

CREATE TABLE wallets (
                         id BIGSERIAL PRIMARY KEY,
                         phone_number VARCHAR(20),
                         salary NUMERIC(10, 2),
                         balance NUMERIC(10, 2),
                         created_at TIMESTAMP WITHOUT TIME ZONE,
                         wallet_type VARCHAR(50),
                         user_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO wallets (id, wallet_type, salary, balance, created_at, user_id, phone_number) VALUES
(1, 'CURRENT', 3000.00, 11946.00, '2024-02-14 19:06:29', 1, '89033543683'),
(2, 'CREDIT', 2500.00, 8101.00, '2024-02-14 19:06:31', 2, '89999897654');

CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              amount NUMERIC(10, 2),
                              currency VARCHAR(50),
                              date_time TIMESTAMP WITHOUT TIME ZONE,
                              sender_phone_number VARCHAR(20),
                              recipient_phone_number VARCHAR(20),
                              status VARCHAR(50),
                              type VARCHAR(50),
                              description TEXT,
                              user_id BIGINT,
                              FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE subscriptions (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(255),
                               price NUMERIC(10, 2),
                               start_date TIMESTAMP WITHOUT TIME ZONE,
                               end_date TIMESTAMP WITHOUT TIME ZONE,
                               status VARCHAR(50),
                               type VARCHAR(50),
                               payment_frequency VARCHAR(50),
                               auto_renew BOOLEAN,
                               wallet_number VARCHAR(20),
                               user_id BIGINT,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

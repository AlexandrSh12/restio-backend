CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255),
                       active BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            roles VARCHAR(50) NOT NULL,
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) UNIQUE NOT NULL,
                          description VARCHAR(255)
);

CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      category_id BIGINT NOT NULL,
                      price DECIMAL(10,2) NOT NULL,
                      cook_time INT,
                      image_url VARCHAR(255),
                      CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id)
);

INSERT INTO users (id, username, password, full_name) VALUES
                                                          (1, 'admin', '$2a$10$AxpqS352RRHZaJ2z4CKpUOEnrU/EqbsAMsnvsG5v5E8vsM7vlrZ8q', 'Администратор Системы'),
                                                          (2, 'waiter', '$2a$10$O27NAQZ03BxuZj6s25e9LOnn7jMXTm8S0BqyRdXG2pIyh.GDHyUUm', 'Официант Иван'),
                                                          (3, 'chef', '$2a$10$EH.csMFOvNWIJr1hvZiBce3FODeUR8GjUTzIbcdT9YjVfu/8cNXle', 'Повар Виктор');

INSERT INTO user_roles (user_id, roles) VALUES
                                            (1, 'ADMIN'),
                                            (2, 'WAITER'),
                                            (3, 'CHEF');

INSERT INTO category (id, name, description) VALUES
                                                 (1, 'Пицца', 'Итальянские пиццы на тонком тесте'),
                                                 (2, 'Паста', 'Разнообразные виды пасты'),
                                                 (3, 'Салаты', 'Свежие салаты'),
                                                 (4, 'Напитки', 'Прохладительные напитки');

-- ИСПРАВЛЕНО: убрал столбец 'category' из INSERT запроса
INSERT INTO dish (name, category_id, price, cook_time, image_url) VALUES
                                                                      ('Пицца Маргарита', 1, 550, 15, 'https://sun9-39.userapi.com/impg/oW5ALbk8jx83-lf1SxF6aV_pbYmDiFvHljiRcw/nM5vXKVx0Co.jpg?size=624x416&quality=95&sign=56ac22b9308a7ec8794037cf667c3e36&type=album'),
                                                                      ('Пицца Пепперони', 1, 650, 15, 'https://upload.wikimedia.org/wikipedia/commons/9/91/Pizza-3007395.jpg'),
                                                                      ('Карбонара', 2, 450, 12, 'https://sun9-79.userapi.com/impg/YnJrk2SyGZdOecX64quz-mr_pMxZXBJOhd0J3w/TVBuvv8nObo.jpg?size=2500x1250&quality=95&sign=b8c1b25c101b59cdbac8b811950ee8bb&type=album'),
                                                                      ('Цезарь', 3, 380, 5, 'https://sun9-80.userapi.com/impg/NJidqhFjfeXPf35KOC4o1Jq0JO_5dcn3HNewTw/-vGguE9eVlI.jpg?size=1342x930&quality=95&sign=4f242e32e049273e23e09b5ad31e4b98&type=album'),
                                                                      ('Кола', 4, 120, 1, 'https://sun9-3.userapi.com/impg/ruHFRuj7RL5_INEEip1ttora4MJmrsOjJBSkDg/jgGRWzpLxZ0.jpg?size=1024x1024&quality=95&sign=3c62f2389a48e1a9c57d14f832caaf09&type=album');
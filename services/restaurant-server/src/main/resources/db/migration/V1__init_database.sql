CREATE SEQUENCE IF NOT EXISTS restaurant_seq INCREMENT BY 1 START 1;
CREATE SEQUENCE IF NOT EXISTS cuisine_types_seq INCREMENT BY 1 START 1;

CREATE TABLE IF NOT EXISTS restaurant
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    opening_hours VARCHAR(50) NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    is_open BOOLEAN NOT NULL,
    price_range VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS cuisine_types
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    restaurant_id INTEGER NOT NULL CONSTRAINT fk_cuisine_types_restaurant REFERENCES restaurant(id)
);


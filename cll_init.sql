DROP TABLE mbCollection;
DROP TABLE users;

CREATE TABLE IF NOT EXISTS users(
id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
password VARCHAR(255),
salt VARCHAR(255),
UNIQUE (name)
);

INSERT INTO users (name, password, salt) VALUES 
('sigma', 'ad66b1942fe42421cbdcb721fa2ea54d6bff587cb7def9c3b123fe29289c86530010ed1d38288622c5afbf62383be52febbdf7e396162c7ef965b2a3d326b4f3', '+iv+GYJ46XJtpVxReWqoCg==');

CREATE TABLE IF NOT EXISTS mbCollection (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coordinate_x INTEGER NOT NULL,
    coordinate_y BIGINT CHECK (coordinate_y <= 40),
    creationDate VARCHAR(255) NOT NULL,
    numberOfParticipants BIGINT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    label_ VARCHAR(255),
    userid INTEGER REFERENCES users(id),
    UNIQUE (name)
);

INSERT INTO mbCollection (name, coordinate_x, coordinate_y, creationDate, numberOfParticipants, genre, label_, userid) VALUES 
('SigmaBoys', 3, 4, '1744129937309', 12, 'JAZZ', 'jjj', 1),
('Skibidi', 3, 4, '1744129937309', 2, 'MATH_ROCK', 'jjj', 1),
('ITMO', 3, 4, '1744129937309', 6, 'POST_PUNK', 'jjj', 1);
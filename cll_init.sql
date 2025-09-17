CREATE TABLE IF NOT EXISTS users(
id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
password VARCHAR(255),
salt VARCHAR(255),
UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS mbCollection (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coordinate_x INTEGER NOT NULL,
    coordinate_y BIGINT CHECK (coordinate_y <= 40),
    creationDate VARCHAR(255) NOT NULL,
    numberOfParticipants BIGINT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    label_ VARCHAR(255),
    userID INTEGER REFERENCES users(id),
    UNIQUE (name)
);

INSERT INTO mbCollection (name, coordinate_x, coordinate_y, creationDate, numberOfParticipants, genre, label_) VALUES 
('SigmaBoys', 3, 4, '1744129937309', 12, 'JAZZ', 'jjj'),
('Skibidi', 3, 4, '1744129937309', 2, 'MATH_ROCK', 'jjj'),
('ITMO', 3, 4, '1744129937309', 6, 'POST_PUNK', 'jjj');
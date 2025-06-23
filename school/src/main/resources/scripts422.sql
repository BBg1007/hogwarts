CREATE TABLE cars (
car_id SERIAL PRIMARY KEY,
brand VARCHAR(50) NOT NULL,
model VARCHAR(50) NOT NULL,
price DECIMAL(12, 2) NOT NULL,
CONSTRAINT price_positive);

CREATE TABLE persons (
person_id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
age INTEGER NOT NULL,
has_license BOOLEAN NOT NULL,
CONSTRAINT valid_age CHECK (age => 18));

CREATE TABLE person_car (
person_id INTEGER REFERENCES persons(person_id),
car_id INTEGER REFERENCES cars(car_id),
PRIMARY KEY (person_id, car_id));








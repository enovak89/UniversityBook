CREATE TABLE people (
                        id serial PRIMARY KEY,
                        name varchar,
                        age int,
                        license boolean
);
CREATE TABLE cars (
                      id serial PRIMARY KEY,
                      brand varchar,
                      model varchar,
                      price money
);
CREATE TABLE people_cars_reference (
                                       id serial PRIMARY KEY,
                                       people_id int REFERENCES people (id),
                                       car_id int REFERENCES cars (id)
);
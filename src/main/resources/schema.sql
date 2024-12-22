CREATE TYPE user_role AS ENUM ('OWNER', 'TENANT');

CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password TEXT NOT NULL,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          phone CHAR(10) NOT NULL UNIQUE,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          afm CHAR(10) NOT NULL UNIQUE,
                          id_front_path VARCHAR(255),
                          id_back_path VARCHAR(255),
                          role user_role NOT NULL -- Use ENUM for role
);

CREATE TABLE properties (
                            id SERIAL PRIMARY KEY,
                            category VARCHAR(20) NOT NULL,
                            area VARCHAR(100) NOT NULL,
                            address VARCHAR(255) NOT NULL,
                            atak CHAR(10) NOT NULL UNIQUE,
                            price NUMERIC(10, 2) NOT NULL CHECK (price > 0),
                            square_meters INTEGER NOT NULL CHECK (square_meters > 0),
                            floor INTEGER NOT NULL CHECK (floor >= 0 AND floor <= 10),
                            number_of_rooms INTEGER NOT NULL CHECK (number_of_rooms >= 1 AND number_of_rooms <= 10),
                            number_of_bathrooms INTEGER NOT NULL CHECK (number_of_bathrooms >= 1 AND number_of_bathrooms <= 10),
                            renovation_year INTEGER NOT NULL CHECK (renovation_year BETWEEN 1900 AND 2100),
                            approved BOOLEAN NOT NULL DEFAULT FALSE,
                            owner_id INTEGER NOT NULL REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TYPE feature AS ENUM ('ELEVATOR', 'PARKING', 'BARBECUE', 'SWIMMING_POOL');

CREATE TABLE property_features (
                                   property_id INTEGER NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
                                   feature feature NOT NULL,
                                   PRIMARY KEY (property_id, feature)
);

CREATE TABLE photos (
                        id SERIAL PRIMARY KEY,
                        file_path VARCHAR(255) NOT NULL,
                        property_id INTEGER NOT NULL REFERENCES properties(id) ON DELETE CASCADE
);

CREATE TABLE viewing_requests (
                                  id SERIAL PRIMARY KEY,
                                  property_id INTEGER NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
                                  tenant_id INTEGER NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                                  proposed_time TIMESTAMP NOT NULL,
                                  status VARCHAR(20) NOT NULL
);

CREATE TABLE rental_requests (
                                 id SERIAL PRIMARY KEY,
                                 property_id INTEGER NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
                                 tenant_id INTEGER NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                                 status VARCHAR(20) NOT NULL
);

CREATE TABLE availability_slot (
                                   id SERIAL PRIMARY KEY,
                                   property_id INTEGER NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
                                   start_time TIMESTAMP NOT NULL,
                                   end_time TIMESTAMP NOT NULL
);

-- Query
SELECT * FROM app_user WHERE role = 'ADMIN';

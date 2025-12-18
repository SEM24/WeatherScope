CREATE TABLE weather_data (
                              id SERIAL PRIMARY KEY,
                              city VARCHAR(100),
                              temperature DOUBLE PRECISION,
                              humidity DOUBLE PRECISION,
                              description VARCHAR(255),
                              timestamp TIMESTAMP
);

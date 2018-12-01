CREATE TABLE arduino_command (
    id SERIAL PRIMARY KEY,
    arduino_id BIGINT REFERENCES arduino(id),
    command_number BIGINT,
    command_title TEXT NOT NULL,
    command_description TEXT NOT NULL
)
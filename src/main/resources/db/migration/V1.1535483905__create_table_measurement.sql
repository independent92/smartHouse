CREATE TABLE measurement (
  id SERIAL PRIMARY KEY,
  sensor_id BIGINT,
  value NUMERIC,
  unit TEXT
)
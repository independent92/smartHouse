CREATE TABLE role (
  id SERIAL PRIMARY KEY,
  role TEXT NOT NULL UNIQUE
);

INSERT INTO role (role) VALUES ('admin');
INSERT INTO role (role) VALUES ('user');
CREATE TABLE arduino (
id SERIAL PRIMARY KEY,
port_name TEXT,
description TEXT NOT NULL
);

INSERT INTO arduino (description) VALUES ('arduino with defferent basic sensors');
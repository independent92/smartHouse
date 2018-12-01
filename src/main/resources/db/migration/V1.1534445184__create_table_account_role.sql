CREATE TABLE account_role (
  id SERIAL PRIMARY KEY,
  account_id BIGINT REFERENCES account (id),
  role_id BIGINT REFERENCES role (id)
)
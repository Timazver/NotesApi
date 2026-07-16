ALTER TABLE users
    ADD COLUMN role VARCHAR(10) not null default 'USER';
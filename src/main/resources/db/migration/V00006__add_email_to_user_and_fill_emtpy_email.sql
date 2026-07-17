UPDATE users u
SET email = ac.email
FROM auth_credentials ac
WHERE ac.user_id = u.id;

DELETE
FROM users
WHERE email IS NULL;

ALTER TABLE users
    ALTER COLUMN email
        SET not null;

ALTER TABLE users
    ADD CONSTRAINT users_email_key UNIQUE (email);
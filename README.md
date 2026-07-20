# Notes API

## Bootstrap the first administrator

The administrator bootstrap is a one-time command. It is disabled during a normal application start and becomes active
only with the `bootstrap-admin` Spring profile.

Run the command from the directory that contains the production `compose.yaml` and `.env` files:

```bash
read -s ADMIN_BOOTSTRAP_PASSWORD
export ADMIN_BOOTSTRAP_PASSWORD



unset ADMIN_BOOTSTRAP_PASSdocker compose run --rm \
  -e ADMIN_BOOTSTRAP_PASSWORD \
  notes-api \
  --spring.profiles.active=bootstrap-admin \
  --spring.main.web-application-type=none \
  --first-name="Admin" \
  --last-name="User" \
  --email="admin@example.com"WORD
```

The password is not passed as a command-line argument and is not stored in the project `.env` file. The temporary
bootstrap container is removed after the command completes.

If an administrator already exists, the command exits without changing the database. If the specified email belongs to a
non-admin user, the command fails instead of promoting that user implicitly.

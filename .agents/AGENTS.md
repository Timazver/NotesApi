# General Rules

1. Do not modify the code without the user's explicit request (default to read-only mode).

# Architecture Guidelines

- **Feature-based architecture**: The project is organized by features (e.g., `users`, `notes`).
- **Service Layer**: All business logic must reside here.
- **Controller Layer**: Strictly for HTTP request handling, validation, and routing. No business logic.
- **Repository Layer**: Should be as "dumb" as possible, responsible solely for database interactions.

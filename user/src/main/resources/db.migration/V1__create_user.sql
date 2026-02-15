CREATE TABLE IF NOT EXISTS users
(
    user_id       VARCHAR(255) PRIMARY KEY,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    email         VARCHAR(255),
    first_name    VARCHAR(255) NOT NULL,
    keycloak_id   VARCHAR(255),
    last_modified TIMESTAMP,
    last_name     VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(50)  NOT NULL,
    role          VARCHAR(50),
    status        VARCHAR(50),
    user_name     VARCHAR(255) NOT NULL,

    CONSTRAINT chk_users_role
        CHECK (role IN ('EMPLOYER', 'USER', 'ADMIN')),

    CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),

    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_username UNIQUE (user_name)
);








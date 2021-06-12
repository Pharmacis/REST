-- Drop tables
DROP TABLE if exists user_roles;
DROP TABLE if exists roles;
DROP TABLE if exists users;

-- Table: users
CREATE TABLE users
(
    id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
     profession VARCHAR(255) NOT NULL
)
    ENGINE = InnoDB;

-- Table: roles
CREATE TABLE roles
(
    id   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(100) NOT NULL
)
    ENGINE = InnoDB;

-- Table for mapping user and roles: user_roles
CREATE TABLE user_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),

    UNIQUE (user_id, role_id)
)
    ENGINE = InnoDB;

-- Insert data
INSERT INTO users
VALUES (1, 'User1', '$2a$10$jDXTKSdKGaGMRlzxRmy5ze9QGjRjNMq2ub5CJVJnN6XQQtZ2DhY3q','manager'); --  PASSWORD = 123
INSERT INTO users
VALUES (2, 'UserTest', '$$2a$10$jDXTKSdKGaGMRlzxRmy5ze9QGjRjNMq2ub5CJVJnN6XQQtZ2DhY3q','manager'); --PASSWORD = 123
INSERT INTO users
VALUES (3, 'UserAdmin', '$2a$10$jDXTKSdKGaGMRlzxRmy5ze9QGjRjNMq2ub5CJVJnN6XQQtZ2DhY3q','director'); -- PASSWORD = 123

INSERT INTO roles
VALUES (1, 'ROLE_USER');
INSERT INTO roles
VALUES (2, 'ROLE_ADMIN');

INSERT INTO users_roles
VALUES (1, 2);
INSERT INTO users_roles
VALUES (2, 1);
INSERT INTO users_roles
VALUES (3, 2);
INSERT INTO users_roles
VALUES (3, 1);
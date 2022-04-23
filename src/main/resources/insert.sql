INSERT INTO users (email, first_name, last_name, username, password, phone_number) VALUES ('user@p.pl', 'username', 'usersurname', 'user', '$2a$12$aQa1H5e.cUndJSPVcsAhsu0NtjMHcWLRfIdLLP4Bn0ujQ8t1rSMey', 111222333);
INSERT INTO users (email, first_name, last_name, username, password, phone_number) VALUES ('admin@p.pl', 'adminname', 'adminsurname', 'admin', '$2a$12$SF4tzt4Gs4Y8dehVFKpSZOFUxqwG7SC/xnEFS40RSn6dUshuc7IbW', 111222333);

INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (2,2);

INSERT INTO investity (budget, investity_name, admin_id) VALUES (100000, 'Inwestycja 1', 2);
INSERT INTO investity (budget, investity_name, admin_id) VALUES (150000, 'Inwestycja 2', 2);
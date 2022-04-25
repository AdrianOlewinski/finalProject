INSERT INTO users (email, first_name, last_name, username, password, phone_number, salary_per_hours) VALUES ('user@p.pl', 'username', 'usersurname', 'user', '$2a$10$TSNzMKGsyYiPmpcl4X7esOvsv3gx8Du2RfFkADIaLMFcrArJjVgj.', 111222333, 20);
INSERT INTO users (email, first_name, last_name, username, password, phone_number, salary_per_hours) VALUES ('admin@p.pl', 'adminname', 'adminsurname', 'admin', '$2a$12$SF4tzt4Gs4Y8dehVFKpSZOFUxqwG7SC/xnEFS40RSn6dUshuc7IbW', 111222333, 20);
INSERT INTO users (email, first_name, last_name, username, password, phone_number, salary_per_hours) VALUES ('user2@p.pl', 'username2', 'usersurname2', 'user2', '$2a$10$fHvOEyzbLJEhSZaaHaQoLO9FkrHAF/NU1APpEl6rMIsklxiFEQ.Iy', 111222333, 20);

INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_INACTIVE');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (2,2);
INSERT INTO user_roles (user_id, role_id) VALUES (3,1);

INSERT INTO investity (budget, investity_name, admin_id) VALUES (100000, 'Inwestycja 1', 2);
INSERT INTO investity (budget, investity_name, admin_id) VALUES (150000, 'Inwestycja 2', 2);

INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user) VALUES ('2022-04-24',10,1,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user) VALUES ('2022-04-23',7,2,1);

INSERT INTO supplier (name, type, description) VALUES ('Dostawca 1', 'Dostawca', 'Opis 1');
INSERT INTO supplier (name, type, description) VALUES ('Podwykonawca 1', 'Podwykonawca', 'Opis 1');
INSERT INTO supplier (name, type, description) VALUES ('Dostawca 2', 'Dostawca', 'Opis 2');

INSERT INTO investity_costs (cost, description, ivestity_id, suplier_id) VALUES (500, 'opis 1', 1, 1);
INSERT INTO investity_costs (cost, description, ivestity_id, suplier_id) VALUES (1000, 'opis 2', 1, 2);
INSERT INTO investity_costs (cost, description, ivestity_id, suplier_id) VALUES (2000, 'opis 3', 1, 3);
INSERT INTO users (email, first_name, last_name, username, password, phone_number, salary_per_hours) VALUES ('jkowalski@wp.pl', 'Jan', 'Kowalski', 'user', '$2a$10$TSNzMKGsyYiPmpcl4X7esOvsv3gx8Du2RfFkADIaLMFcrArJjVgj.', 111222333, 20);
INSERT INTO users (email, first_name, last_name, username, password, phone_number, salary_per_hours) VALUES ('aolewinski@wp.pl', 'Adrian', 'Olewiński', 'admin', '$2a$12$SF4tzt4Gs4Y8dehVFKpSZOFUxqwG7SC/xnEFS40RSn6dUshuc7IbW', 111222333, 20);
INSERT INTO users (email, first_name, last_name, username, password, phone_number, salary_per_hours) VALUES ('anowak@wp.pl', 'Andrzej', 'Nowak', 'user2', '$2a$10$fHvOEyzbLJEhSZaaHaQoLO9FkrHAF/NU1APpEl6rMIsklxiFEQ.Iy', 111222333, 20);

INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_INACTIVE');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (2,2);
INSERT INTO user_roles (user_id, role_id) VALUES (3,1);

INSERT INTO investity (budget, investity_name, admin_id) VALUES (100000, 'Wilanowska', 2);
INSERT INTO investity (budget, investity_name, admin_id) VALUES (150000, 'Sitel', 2);

INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-04-24',10,1,1,20,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-04-23',7,2,1,20,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-03-23',10,2,1,20,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-02-23',12,2,1,20,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-03-23',8,2,1,20,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-04-23',10,2,1,20,1);
INSERT INTO working_time (local_date, number_of_hours, id_investity, id_user, salary_per_hours, multiplier) VALUES ('2022-04-23',10,2,3,20,1);

INSERT INTO supplier (name, type, description) VALUES ('Onninen', 'Dostawca', 'Hurtownia elektryczna');
INSERT INTO supplier (name, type, description) VALUES ('Kamyk', 'Podwykonawca', 'Zakes teletechniczny');
INSERT INTO supplier (name, type, description) VALUES ('Keren', 'Dostawca', 'Dostawca lamp');

INSERT INTO investity_costs (cost, description, ivestity_id, supplier_id) VALUES (500, 'Dostawa materiałów', 1, 1);
INSERT INTO investity_costs (cost, description, ivestity_id, supplier_id) VALUES (1000, 'Zakres: SSP, CCTV, SSWIN, przyzywówka, LAN', 1, 2);
INSERT INTO investity_costs (cost, description, ivestity_id, supplier_id) VALUES (2000, 'Dostawa lamp oświetleniowych', 1, 3);
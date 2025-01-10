INSERT INTO users(is_active, first_name, last_name, password, username)
VALUES (TRUE, 'admin1', 'admin1', '123', 'admin1.admin1');

INSERT INTO trainees(date_of_birth, user_id, address)
VALUES ('2005-01-01', 1, 'Test address');

INSERT INTO training_types(training_type_name)
VALUES ('GYM');

INSERT INTO users(is_active, first_name, last_name, password, username)
VALUES (TRUE, 'admin2', 'admin2', '123', 'admin2.admin2');

INSERT INTO trainers(training_type_id, user_id)
VALUES (1, 2);

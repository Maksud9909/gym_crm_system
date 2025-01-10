INSERT INTO training_types(training_type_name)
VALUES ('GYM');

INSERT INTO users(is_active, first_name, last_name, password, username)
VALUES (TRUE, 'admin', 'admin', '123','admin.admin');

INSERT INTO trainers(training_type_id, user_id)
VALUES (1,1);


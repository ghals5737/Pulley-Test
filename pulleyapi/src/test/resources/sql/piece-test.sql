INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (1, 'MATH01', 1 , 'SUBJECTIVE' , 'answer1', floor(rand() * 100000000));
INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (2, 'MATH01', 2 , 'SELECTION' , 'answer2' , floor(rand() * 100000000));
INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (3, 'SCI01', 3 , 'SELECTION' , 'answer3' , floor(rand() * 100000000));
INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (4, 'SCI01', 4 , 'SUBJECTIVE' , 'answer4' , floor(rand() * 100000000));
INSERT INTO teachers (id, name) VALUES (11, 'TeacherA');
INSERT INTO teachers (id, name) VALUES (12, 'TeacherB');
INSERT INTO students (id, name) VALUES (11, 'StudentA');
INSERT INTO students (id, name) VALUES (12, 'StudentB');
INSERT INTO students (id, name) VALUES (13, 'StudentC');
-- INSERT INTO pieces (id, name, teacher_id) VALUES (2, 'Piece1', 1);
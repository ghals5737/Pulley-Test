INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (1, 'MATH01', 1 , 'SUBJECTIVE' , 'answer1', floor(rand() * 100000000));
INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (2, 'MATH01', 2 , 'SELECTION' , 'answer2' , floor(rand() * 100000000));
INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (3, 'SCI01', 3 , 'SELECTION' , 'answer3' , floor(rand() * 100000000));
INSERT INTO PROBLEMS (id, unit_code, level, problem_type, answer, rand_id) VALUES (4, 'SCI01', 4 , 'SUBJECTIVE' , 'answer4' , floor(rand() * 100000000));
INSERT INTO teachers (id, name) VALUES (1, 'TeacherA');
INSERT INTO students (id, name) VALUES (1, 'StudentA');
INSERT INTO students (id, name) VALUES (2, 'StudentB');
INSERT INTO students (id, name) VALUES (3, 'StudentC');
INSERT INTO pieces (id, name, teacher_id) VALUES (1, 'Piece1', 1);
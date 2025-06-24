--1 Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах (достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.
SELECT s.name AS student_name,  s.age AS student_age, f.name AS faculty_name FROM student s JOIN faculty f ON s.faculty_id = f.id;
--2 Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.
SELECT s.name AS student_name,  s.age AS student_age, a.file_path AS avatar_path FROM student s JOIN avatar a ON a.student_id = s.id;

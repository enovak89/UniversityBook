SELECT student.name AS student_name, student.age, faculty.name AS faculty_name
FROM student
         INNER JOIN faculty ON student.faculty_id = faculty.id;
SELECT student.id, student.name
FROM student
         RIGHT JOIN avatar ON student.id = avatar.student_id;
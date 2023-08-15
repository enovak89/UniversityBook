package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentsAverageAge;
import ru.hogwarts.school.model.StudentsCount;
import ru.hogwarts.school.model.StudentsOrderedByIdDescLimit;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findAllByAge(Integer age);

    Collection<Student> findAllByAgeBetween(Integer min, Integer max);

    Collection<Student> findAllByFaculty_Id(Long id);

    @Query(value = "SELECT count(id) FROM student", nativeQuery = true)
    StudentsCount getStudentsCount();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    StudentsAverageAge getStudentAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<StudentsOrderedByIdDescLimit> getStudentsByIdDescLimit();

}

package ru.hogwarts.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {StudentService.class})
@ExtendWith(SpringExtension.class)
class StudentServiceTest {

    private final StudentService studentService;
    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceTest(StudentService studentService) {
        this.studentService = studentService;
    }

    private static final Student CORRECT_STUDENT = new Student();

    @PostConstruct
    private void initData() {
        CORRECT_STUDENT.setId(1L);
        CORRECT_STUDENT.setName("Petr");
        CORRECT_STUDENT.setAge(20);
    }

    @Test
    void createStudentCorrect() {
        Student expectedResult = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);

        when(studentRepository.save(CORRECT_STUDENT)).thenReturn(CORRECT_STUDENT);

        Assertions.assertEquals(expectedResult, studentService.createStudent(CORRECT_STUDENT));
    }

    @Test
    void getStudentByIdCorrect() {
        Student expectedResult = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);

        when(studentRepository.findById(CORRECT_STUDENT.getId())).thenReturn(Optional.of(CORRECT_STUDENT));

        Assertions.assertEquals(expectedResult, studentService.getStudentById(expectedResult.getId()).get());
    }

    @Test
    void editStudentCorrect() {
        Student expectedResult = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(25);

        when(studentRepository.save(expectedResult)).thenReturn(expectedResult);

        Assertions.assertEquals(expectedResult, studentService.editStudent(expectedResult));
    }

    @Test
    void removeStudentCorrect() {
        Student expectedResult = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);

        when(studentRepository.findById(expectedResult.getId())).thenReturn(Optional.of(expectedResult));

        Assertions.assertEquals(expectedResult, studentService.removeStudent(expectedResult.getId()));
    }

    @Test
    void getAllStudentCorrect() {
        List<Student> expectedList = new ArrayList<>();
        Student expectedResult = new Student();
        Student expectedResultSecond = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);
        expectedResultSecond.setId(2L);
        expectedResultSecond.setName("Ivan");
        expectedResultSecond.setAge(20);
        expectedList.add(expectedResult);
        expectedList.add(expectedResultSecond);

        when(studentRepository.findAll()).thenReturn(expectedList);

        Assertions.assertEquals(expectedList, studentService.getAllStudent());
    }

    @Test
    void getAllStudentNullResult() {
        when(studentRepository.findAll()).thenReturn(null);

        Assertions.assertNull(studentService.getAllStudent());
    }

    @Test
    void getStudentByColor() {
        List<Student> expectedList = new ArrayList<>();
        Student expectedResult = new Student();
        Student expectedResultSecond = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);
        expectedResultSecond.setId(2L);
        expectedResultSecond.setName("Ivan");
        expectedResultSecond.setAge(20);
        expectedList.add(expectedResult);
        expectedList.add(expectedResultSecond);

        when(studentRepository.findAllByAge(expectedResult.getAge())).thenReturn(expectedList);

        Assertions.assertEquals(expectedList, studentService.getStudentByAge(expectedResult.getAge()));
    }

    @Test
    void getStudentByAgeBetweenCorrect() {
        List<Student> expectedList = new ArrayList<>();
        Student expectedResult = new Student();
        Student expectedResultSecond = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);
        expectedResultSecond.setId(2L);
        expectedResultSecond.setName("Ivan");
        expectedResultSecond.setAge(25);
        expectedList.add(expectedResult);
        expectedList.add(expectedResultSecond);

        when(studentRepository.findAllByAgeBetween(18, 65)).thenReturn(expectedList);

        Assertions.assertEquals(expectedList, studentService.getStudentByAgeBetween(18, 65));
    }

    @Test
    void getStudentFacultyCorrect() {
        Faculty expectedResult = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");
        Student expectedStudentResult = new Student();
        expectedStudentResult.setId(1L);
        expectedStudentResult.setName("Ivan");
        expectedStudentResult.setAge(25);
        expectedStudentResult.setFaculty(expectedResult);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(expectedStudentResult));

        Assertions.assertEquals(expectedResult, studentService.getStudentFaculty(1L));
    }
}
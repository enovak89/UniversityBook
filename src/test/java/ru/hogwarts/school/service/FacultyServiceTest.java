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
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {FacultyService.class})
@ExtendWith(SpringExtension.class)
class FacultyServiceTest {

    private final FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    public FacultyServiceTest(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    private static final Faculty CORRECT_FACULTY = new Faculty();

    @PostConstruct
    private void initData() {
        CORRECT_FACULTY.setId(1L);
        CORRECT_FACULTY.setName("Hogwarts");
        CORRECT_FACULTY.setColor("Red");
    }

    @Test
    void createFacultyCorrect() {
        Faculty expectedResult = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");

        when(facultyRepository.save(CORRECT_FACULTY)).thenReturn(CORRECT_FACULTY);

        Assertions.assertEquals(expectedResult, facultyService.createFaculty(CORRECT_FACULTY));
    }

    @Test
    void getFacultyByIdCorrect() {
        Faculty expectedResult = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");

        when(facultyRepository.findById(CORRECT_FACULTY.getId())).thenReturn(Optional.of(CORRECT_FACULTY));

        Assertions.assertEquals(expectedResult, facultyService.getFacultyById(expectedResult.getId()).get());
    }

    @Test
    void editFacultyCorrect() {
        Faculty expectedResult = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Blue");

        when(facultyRepository.save(expectedResult)).thenReturn(expectedResult);

        Assertions.assertEquals(expectedResult, facultyService.editFaculty(expectedResult));
    }

    @Test
    void removeFacultyCorrect() {
        Faculty expectedResult = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");

        when(facultyRepository.findById(expectedResult.getId())).thenReturn(Optional.of(expectedResult));

        Assertions.assertEquals(expectedResult, facultyService.removeFaculty(expectedResult.getId()));
    }

    @Test
    void getAllFacultyCorrect() {
        List<Faculty> expectedList = new ArrayList<>();
        Faculty expectedResult = new Faculty();
        Faculty expectedResultSecond = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");
        expectedResultSecond.setId(2L);
        expectedResultSecond.setName("Puff");
        expectedResultSecond.setColor("Red");
        expectedList.add(expectedResult);
        expectedList.add(expectedResultSecond);

        when(facultyRepository.findAll()).thenReturn(expectedList);

        Assertions.assertEquals(expectedList, facultyService.getAllFaculty());
    }

    @Test
    void getAllFacultyNullResult() {
        when(facultyRepository.findAll()).thenReturn(null);

        Assertions.assertNull(facultyService.getAllFaculty());
    }

    @Test
    void getFacultyByColorCorrect() {
        Collection<Faculty> expectedList = new ArrayList<>();
        Faculty expectedResult = new Faculty();
        Faculty expectedResultSecond = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");
        expectedResultSecond.setId(2L);
        expectedResultSecond.setName("Puff");
        expectedResultSecond.setColor("Red");
        expectedList.add(expectedResult);
        expectedList.add(expectedResultSecond);

        when(facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(expectedResult.getColor(), null)).thenReturn(expectedList);

        Assertions.assertEquals(expectedList, facultyService.getFacultyByColorOrName(expectedResult.getColor(), null));
    }

    @Test
    void getFacultyByNameCorrect() {
        Collection<Faculty> expectedList = new ArrayList<>();
        Faculty expectedResult = new Faculty();
        expectedResult.setId(1L);
        expectedResult.setName("Hogwarts");
        expectedResult.setColor("Red");
        expectedList.add(expectedResult);

        when(facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(null, expectedResult.getName())).thenReturn(expectedList);

        Assertions.assertEquals(expectedList, facultyService.getFacultyByColorOrName(null, expectedResult.getName()));
    }

    @Test
    void getStudentsByFacultyIdCorrect() {
        List<Student> expectedStudentList = new ArrayList<>();
        Student expectedResult = new Student();
        Student expectedResultSecond = new Student();
        expectedResult.setId(1L);
        expectedResult.setName("Petr");
        expectedResult.setAge(20);
        expectedResultSecond.setId(2L);
        expectedResultSecond.setName("Ivan");
        expectedResultSecond.setAge(20);
        expectedStudentList.add(expectedResult);
        expectedStudentList.add(expectedResultSecond);
        Faculty expectedFacultyResult = new Faculty();
        expectedFacultyResult.setId(1L);
        expectedFacultyResult.setName("Hogwarts");
        expectedFacultyResult.setColor("Red");
        expectedFacultyResult.setStudents(expectedStudentList);

        when(studentRepository.findAllByFaculty_Id(1L)).thenReturn(expectedStudentList);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(expectedFacultyResult));

        Assertions.assertEquals(expectedStudentList, facultyService.getStudentsByFacultyId(1L));
    }

}
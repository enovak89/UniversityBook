package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;
    private final StudentController studentController;
    private final MockMvc mockMvc;

    @Autowired
    StudentControllerTest(StudentController studentController, MockMvc mockMvc) {
        this.studentController = studentController;
        this.mockMvc = mockMvc;
    }

    private static final Student CORRECT_STUDENT = new Student();
    private static final List<Student> CORRECT_STUDENT_COLLECTION = new ArrayList<>();
    private static final Faculty CORRECT_FACULTY = new Faculty();
    private static final JSONObject studentObject = new JSONObject();
    private static final JSONObject facultyObject = new JSONObject();

    @PostConstruct
    public void initData() throws JSONException {
        Long id = 1L;
        String STUDENT_NAME = "Alex";
        int STUDENT_AGE = 20;
        String FACULTY_NAME = "First";
        String FACULTY_COLOR = "Red";

        CORRECT_STUDENT.setId(id);
        CORRECT_STUDENT.setName(STUDENT_NAME);
        CORRECT_STUDENT.setAge(STUDENT_AGE);

        CORRECT_FACULTY.setColor(FACULTY_COLOR);
        CORRECT_FACULTY.setId(id);
        CORRECT_FACULTY.setName(FACULTY_NAME);

        CORRECT_STUDENT.setFaculty(CORRECT_FACULTY);

        CORRECT_STUDENT_COLLECTION.clear();
        CORRECT_STUDENT_COLLECTION.add(CORRECT_STUDENT);

        facultyObject.put("id", CORRECT_FACULTY.getId());
        facultyObject.put("name", CORRECT_FACULTY.getName());
        facultyObject.put("color", CORRECT_FACULTY.getColor());
        studentObject.put("id", CORRECT_STUDENT.getId());
        studentObject.put("name", CORRECT_STUDENT.getName());
        studentObject.put("age", CORRECT_STUDENT.getAge());
        studentObject.put("faculty", facultyObject);
    }

    @Test
    void getStudentByIdCorrect() throws Exception {

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_STUDENT));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/id/1/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("$.age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void getStudentNullByIdIncorrect() throws Exception {
        when(studentService.getStudentById(any(Long.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/id/1/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getStudentFacultyByIdCorrect() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_STUDENT));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/faculty-by-id")
                        .param("id", CORRECT_STUDENT.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("@.name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("@.color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void getStudentFacultyByIdIncorrect() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/facultyById")
                        .param("id", CORRECT_STUDENT.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getStudentByAgeCorrect() throws Exception {
        when(studentRepository.findAllByAge(any(int.class))).thenReturn(CORRECT_STUDENT_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age/20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("[0].age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void getStudentByAgeBetween() throws Exception {
        when(studentRepository.findAllByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(CORRECT_STUDENT_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age")
                        .param("min", "0")
                        .param("max", "100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("[0].age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void addStudentCorrect() throws Exception {
        Mockito.when(studentRepository.save(CORRECT_STUDENT)).thenReturn(CORRECT_STUDENT);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("$.age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void editStudent() throws Exception {
        Mockito.when(studentRepository.save(CORRECT_STUDENT)).thenReturn(CORRECT_STUDENT);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("$.age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void removeStudentCorrect() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_STUDENT));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1")
                        .content(studentObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("$.age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void clearStudentCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllStudentCorrect() throws Exception {
        when(studentRepository.findAll()).thenReturn(CORRECT_STUDENT_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("[0].age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void getStudentNameWithACorrect() throws Exception {
        when(studentRepository.findAll()).thenReturn(CORRECT_STUDENT_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/names-starts-with-A")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(CORRECT_STUDENT.getName().toUpperCase()));
    }

    @Test
    void getStudentAverageAgeByStreamApiCorrect() throws Exception {
        when(studentRepository.findAll()).thenReturn(CORRECT_STUDENT_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/average-age-by-stream")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(CORRECT_STUDENT.getAge()));
    }
}
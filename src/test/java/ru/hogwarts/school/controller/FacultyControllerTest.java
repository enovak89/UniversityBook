package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    private final MockMvc mockMvc;

    private final FacultyController facultyController;

    @Autowired
    FacultyControllerTest(MockMvc mockMvc, FacultyController facultyController) {
        this.mockMvc = mockMvc;
        this.facultyController = facultyController;
    }

    private static final Student CORRECT_STUDENT = new Student();
    private static final List<Student> CORRECT_STUDENT_COLLECTION = new ArrayList<>();
    private static final List<Faculty> CORRECT_FACULTY_COLLECTION = new ArrayList<>();
    private static final Faculty CORRECT_FACULTY = new Faculty();
    private static final JSONObject studentObject = new JSONObject();
    private static final JSONObject facultyObject = new JSONObject();

    @PostConstruct
    public void initData() throws JSONException {
        Long id = 1L;
        String STUDENT_NAME = "Petr";
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

        CORRECT_FACULTY_COLLECTION.clear();
        CORRECT_FACULTY_COLLECTION.add(CORRECT_FACULTY);

        facultyObject.put("id", CORRECT_FACULTY.getId());
        facultyObject.put("name", CORRECT_FACULTY.getName());
        facultyObject.put("color", CORRECT_FACULTY.getColor());
        studentObject.put("id", CORRECT_STUDENT.getId());
        studentObject.put("name", CORRECT_STUDENT.getName());
        studentObject.put("age", CORRECT_STUDENT.getAge());
        studentObject.put("faculty", facultyObject);
    }

    @Test
    void getFacultyByIdCorrect() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("$.color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void getFacultyNullByIdIncorrect() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getStudentsByIdCorrect() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_FACULTY));
        when(studentRepository.findAllByFaculty_Id(any(Long.class))).thenReturn(CORRECT_STUDENT_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/students-by-id")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_STUDENT.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_STUDENT.getName()))
                .andExpect(jsonPath("[0].age").value(CORRECT_STUDENT.getAge()));
    }

    @Test
    void getStudentsNullByIdIncorrect() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/studentsById")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addFacultyCorrect() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(CORRECT_FACULTY);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("$.color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void editFacultyCorrect() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(CORRECT_FACULTY);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("$.color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void removeFacultyCorrect() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_FACULTY));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("$.name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("$.color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void clearFacultyCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getFacultyByColorOrNameCorrect() throws Exception {
        when(facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(anyString(), anyString()))
                .thenReturn(CORRECT_FACULTY_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .param("name", "aaa")
                        .param("color", "bbb")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("[0].color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void getFacultyByNullColorOrNullNameCorrect() throws Exception {
        when(facultyRepository.findAll())
                .thenReturn(CORRECT_FACULTY_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_FACULTY.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_FACULTY.getName()))
                .andExpect(jsonPath("[0].color").value(CORRECT_FACULTY.getColor()));
    }

    @Test
    void getFacultyLongestNameCorrect() throws Exception {
        when(facultyRepository.findAll())
                .thenReturn(CORRECT_FACULTY_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/longest-name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(CORRECT_FACULTY.getName()));
    }

    @Test
    void getIntegerNumberCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/integer-number")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1784293664));
    }
}
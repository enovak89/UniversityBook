package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method createFaculty");
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFacultyById(Long id) {
        logger.info("Was invoked method getFacultyById");
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method editFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        logger.info("Was invoked method removeFaculty");
        Faculty faculty = facultyRepository.findById(id).get();
        facultyRepository.delete(faculty);
        return faculty;
    }

    public Collection<Faculty> getAllFaculty() {
        logger.info("Was invoked method getAllFaculty");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultyByColorOrName(String color, String name) {
        logger.info("Was invoked method getFacultyByColorOrName");
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public void clearFaculty() {
        logger.info("Was invoked method clearFaculty");
        facultyRepository.deleteAll();
    }

    public Collection<Student> getStudentsByFacultyId(Long id) {
        logger.info("Was invoked method getStudentsByFacultyId");
        return studentRepository.findAllByFaculty_Id(facultyRepository.findById(id).orElse(null).getId());
    }

    public String getFacultyLongestName() {
        logger.info("Was invoked method getFacultyLongestName");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(s -> s.length())).get();
    }

    public Integer getIntegerNumber() {
        logger.info("Was invoked method getIntegerNumber");
        Integer result = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b );
        logger.info("Was executed method getIntegerNumber");
        return result;
    }
}

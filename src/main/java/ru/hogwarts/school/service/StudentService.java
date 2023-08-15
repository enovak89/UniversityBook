package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.*;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method createStudent");
        return studentRepository.save(student);
    }

    public Optional<Student> getStudentById(Long id) {
        logger.info("Was invoked method getStudentById");
        return studentRepository.findById(id);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method editStudent");
        return studentRepository.save(student);
    }

    public Student removeStudent(Long id) {
        logger.info("Was invoked method removeStudent");
        Student student = studentRepository.findById(id).get();
        studentRepository.delete(student);
        return student;
    }

    public Collection<Student> getAllStudent() {
        logger.info("Was invoked method getAllStudent");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentByAge(Integer age) {
        logger.info("Was invoked method getStudentByAge");
//        return studentMap.values().stream()
//                .filter(x -> x.getAge() == age)
//                .collect(Collectors.toList());
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> getStudentByAgeBetween(Integer min, Integer max) {
        logger.info("Was invoked method getStudentByAgeBetween");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public void clearStudent() {
        logger.info("Was invoked method clearStudent");
        studentRepository.deleteAll();
    }

    public Faculty getStudentFaculty(Long id) {
        logger.info("Was invoked method getStudentFaculty");
        return studentRepository.findById(id).get().getFaculty();
    }

    public StudentsCount getStudentCount() {
        logger.info("Was invoked method getStudentCount");
        return studentRepository.getStudentsCount();
    }

    public StudentsAverageAge getStudentAge() {
        logger.info("Was invoked method getStudentAge");
        return studentRepository.getStudentAge();
    }

    public List<StudentsOrderedByIdDescLimit> getStudentsByIdDescLimit() {
        logger.info("Was invoked method getStudentsByIdDescLimit");
        return studentRepository.getStudentsByIdDescLimit();
    }

    public List<String> getStudentNameWithA() {
        logger.info("Was invoked method getStudentNameWithA");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A") || name.startsWith("a"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    public Double getStudentAverageAge() {
        logger.info("Was invoked method getStudentAverageAge");
        Integer studentAgeSum = studentRepository.findAll().stream()
                .map(Student::getAge)
                .reduce(0, Integer::sum);
        Long studentCount = studentRepository.findAll().stream().count();
        return (double) studentAgeSum / studentCount;
    }

    public void getStudentNameByThread() {
        List<Student> studentList = studentRepository.findAll();
        printStudentName(studentList.get(0));
        printStudentName(studentList.get(1));

        new Thread(() -> {
            printStudentName(studentList.get(2));
            printStudentName(studentList.get(3));
        }
        ).start();

        new Thread(() -> {
            printStudentName(studentList.get(4));
            printStudentName(studentList.get(5));
        }
        ).start();
    }

    public void getStudentNameBySyncThread() {
        List<Student> studentList = studentRepository.findAll();
        printStudentNameSync(studentList.get(0));
        printStudentNameSync(studentList.get(1));

        new Thread(() -> {
            printStudentNameSync(studentList.get(2));
            printStudentNameSync(studentList.get(3));
        }
        ).start();

        new Thread(() -> {
            printStudentNameSync(studentList.get(4));
            printStudentNameSync(studentList.get(5));
        }
        ).start();
    }

    private void printStudentName(Student student) {
        System.out.println(Thread.currentThread() + " " + student.getName());
    }

    private void printStudentNameSync(Student student) {
        synchronized (this) {
            System.out.println(Thread.currentThread() + " " + student.getName());
        }
    }
}

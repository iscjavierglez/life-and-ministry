package com.javsolutions.jw.lifeandministry;

import com.javsolutions.jw.lifeandministry.model.Student;
import com.javsolutions.jw.lifeandministry.repository.StudentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class HelloController {
    @FXML
    private Label welcomeText;

    private final StudentRepository studentRepository;

    @Autowired
    public HelloController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @FXML
    protected void onHelloButtonClick() {
        try {
            // Create a new student
            Student student = new Student("John Doe", "john.doe@example.com", "North Congregation");

            // Save the student to the database using Spring Data MongoDB
            studentRepository.save(student);

            // Get the count of students in the database
            long count = studentRepository.count();

            // Get all students
            List<Student> students = studentRepository.findAll();

            // Display the result
            welcomeText.setText("Student saved! Total students: " + count);

            // Log the students to the console
            System.out.println("Students in database:");
            for (Student s : students) {
                System.out.println(s);
            }

            // Also show active students count
            long activeCount = studentRepository.countByActiveTrue();
            System.out.println("Active students: " + activeCount);

            // Find student by email
            Student foundStudent = studentRepository.findByEmail("john.doe@example.com");
            if (foundStudent != null) {
                System.out.println("Found student by email: " + foundStudent);
            }
        } catch (Exception e) {
            welcomeText.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentListingController {

    @FXML
    private BorderPane container;

    @FXML
    private Label studentsTitle;

    @FXML
    private ListView<String> studentListView;

    private final StudentApiService studentApiService = new StudentApiService();

    @FXML
    public void initialize() {
        System.out.println("Controller Initialized");
        loadStudents();
    }

    @FXML
    private void openAddStudentView() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/student/student-add-view.fxml", "Home");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadStudents() {
        System.out.println("Loading students...");
        new Thread(() -> {
            JSONArray studentsArray = studentApiService.getAllStudents();
            if (studentsArray != null) {
                Platform.runLater(() -> {
                    studentListView.getItems().clear();
                    for (int i = 0; i < studentsArray.length(); i++) {
                        JSONObject student = studentsArray.getJSONObject(i);
                        String studentId = String.valueOf(student.getInt("studentId"));
                        String name = student.getString("name");
                        String birthPlace = student.getString("birthPlace");
                        String enrollmentStatus = student.getString("enrollmentStatus");

                        // Format the display text for each student
                        String studentInfo = String.format("ID: %s, Name: %s, Birth Place: %s, Status: %s",
                                studentId, name, birthPlace, enrollmentStatus);
                        studentListView.getItems().add(studentInfo);
                    }
                });
            } else {
                System.out.println("Failed to load student data.");
            }
        }).start(); // Run API call in a background thread
    }
}

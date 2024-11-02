package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentListingController {

    @FXML
    private Label studentsTitle;

    @FXML
    private ListView<String> studentListView;

    private final StudentApiService studentApiService = new StudentApiService();

    @FXML
    public void initialize() {
        System.out.println("Controller Initialized");
        loadStudents();

        // Add event listener to handle student selection
        studentListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click to open update form
                String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    openUpdateStudentView(selectedStudent);
                }
            }
        });
    }

    @FXML
    public void openAddStudentView() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/student/student-add-view.fxml", "Add Student");
        } catch (RuntimeException e) {
            System.err.println("Failed to open add student form.");
            e.printStackTrace();
        }
    }

    private void openUpdateStudentView(String studentInfo) {
        // Extract the student ID from the selected item
        String studentIdStr = studentInfo.split(",")[0].split(":")[1].trim();
        Long studentId = Long.parseLong(studentIdStr);

        try {
            // Use ViewManager to switch to the main view (AppController) and pass the studentId
            ViewManager.switchSceneWithData("/com/kss/studentmanagementdesktopclient/view/student/student-update-view.fxml", "Update Student", studentId);
        } catch (RuntimeException e) {
            System.err.println("Failed to open update form.");
            e.printStackTrace();
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

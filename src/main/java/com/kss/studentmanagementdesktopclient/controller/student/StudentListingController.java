package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Controller for the Student Listing view in the Student Management Desktop Client.
 * This JavaFX controller is used with `student-listing-view.fxml` to display a list of students,
 * handle double-click events to open the student update form, and navigate to the add student form.
 */
public class StudentListingController {

    @FXML
    private Label studentsTitle;

    @FXML
    private ListView<String> studentListView;

    private final StudentApiService studentApiService = new StudentApiService();

    /**
     * Initializes the student listing view by loading all students into the list.
     * Adds an event listener for double-clicking on a student entry to open the update view.
     */
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

    /**
     * Opens the add student view, allowing the user to add a new student to the system.
     * This action navigates to the `student-add-view.fxml` file.
     */
    @FXML
    public void openAddStudentView() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/student/student-add-view.fxml", "Add Student");
        } catch (RuntimeException e) {
            System.err.println("Failed to open add student form.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the update view for the selected student by extracting the student ID from the
     * selected item and passing it to the `student-update-view.fxml` file.
     *
     * @param studentInfo the selected student info string from the list, which includes the student ID.
     */
    private void openUpdateStudentView(String studentInfo) {
        // Extract the student ID from the selected item
        String studentIdStr = studentInfo.split(",")[0].split(":")[1].trim();
        Long studentId = Long.parseLong(studentIdStr);

        try {
            ViewManager.switchSceneWithData("/com/kss/studentmanagementdesktopclient/view/student/student-update-view.fxml", "Update Student", studentId);
        } catch (RuntimeException e) {
            System.err.println("Failed to open update form.");
            e.printStackTrace();
        }
    }

    /**
     * Loads all students from the API and displays them in the student list. Each entry
     * in the list contains the student ID, name, birthplace, and enrollment status. The
     * data loading is handled in a background thread to avoid blocking the UI.
     */
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
        }).start();
    }
}

package com.kss.studentmanagementdesktopclient.controller.teacher;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherUpdateController {

    @FXML private TextField teacherNameField;
    @FXML private ListView<String> subjectListView;

    private final TeacherApiService teacherApiService = new TeacherApiService();
    private Long teacherId;  // Store the teacher ID for reference

    /**
     * Load the teacher data based on the provided teacher ID.
     */
    public void loadTeacher(Long teacherId) {
        this.teacherId = teacherId;  // Save the teacher ID for future use
        JSONObject teacherData = teacherApiService.getTeacherById(teacherId);

        if (teacherData != null) {
            // Populate the fields with teacher data
            teacherNameField.setText(teacherData.getString("name"));

            // Populate the subject list
            subjectListView.getItems().clear();
            JSONArray subjects = teacherData.optJSONArray("subjects");
            if (subjects != null) {
                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject subject = subjects.getJSONObject(i);
                    subjectListView.getItems().add(subject.getString("name"));
                }
            }
        } else {
            System.err.println("Teacher data not found for ID: " + teacherId);
        }
    }

    @FXML
    private void handleUpdateTeacher() {
        String name = teacherNameField.getText();

        if (name == null || name.isEmpty()) {
            showAlert("Validation Error", "Teacher name cannot be empty.");
            return;
        }

        // Prepare updated data
        JSONObject updatedTeacher = new JSONObject();
        updatedTeacher.put("name", name);

        // Call API to update teacher
        JSONObject response = teacherApiService.updateTeacher(teacherId, updatedTeacher);

        if (response != null) {
            showAlert("Success", "Teacher updated successfully.");
        } else {
            showAlert("Error", "Failed to update teacher.");
        }
    }

    @FXML
    private void handleDeleteTeacher() {
        boolean confirmed = showConfirmation("Are you sure you want to delete this teacher?");
        if (confirmed) {
            boolean deleted = teacherApiService.deleteTeacher(teacherId);

            if (deleted) {
                showAlert("Success", "Teacher deleted successfully.");
                closeWindow();
            } else {
                showAlert("Error", "Failed to delete teacher.");
            }
        }
    }

    private boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) teacherNameField.getScene().getWindow();
        stage.close();
    }
}

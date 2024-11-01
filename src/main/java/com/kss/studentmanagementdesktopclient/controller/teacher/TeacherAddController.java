package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONObject;

public class TeacherAddController {

    @FXML private TextField teacherNameField;
    @FXML private TextField subjectField;
    @FXML private TextField emailField;

    private final TeacherApiService teacherApiService = new TeacherApiService();

    @FXML
    private void handleAddTeacher() {
        String name = teacherNameField.getText();
        String subject = subjectField.getText();
        String email = emailField.getText();

        // Validate required fields
        if (name == null || name.isEmpty() || subject == null || subject.isEmpty() || email == null || email.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Create JSON object with teacher data
        JSONObject teacherData = new JSONObject();
        teacherData.put("name", name);
        teacherData.put("subject", subject);
        teacherData.put("email", email);

        // Call API to add teacher
        JSONObject response = teacherApiService.addTeacher(teacherData);

        if (response != null) {
            showAlert("Success", "Teacher added successfully.");
            clearForm(); // Clear form after successful submission
        } else {
            showAlert("Error", "Failed to add teacher.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void clearForm() {
        teacherNameField.clear();
        subjectField.clear();
        emailField.clear();
    }
}

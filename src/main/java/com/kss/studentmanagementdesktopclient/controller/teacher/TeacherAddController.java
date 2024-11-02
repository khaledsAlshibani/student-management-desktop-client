package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherAddController {

    @FXML private TextField teacherNameField;

    private final TeacherApiService teacherApiService = new TeacherApiService();

    @FXML
    private void handleAddTeacher() {
        String name = teacherNameField.getText();

        // Validate required fields
        if (name == null || name.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Create JSON object with teacher data
        JSONObject teacherData = new JSONObject();
        teacherData.put("name", name);

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void clearForm() {
        teacherNameField.clear();
    }
}

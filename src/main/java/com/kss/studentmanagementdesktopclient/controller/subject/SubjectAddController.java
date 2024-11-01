package com.kss.studentmanagementdesktopclient.controller.subject;

import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONObject;

public class SubjectAddController {

    @FXML private TextField subjectNameField;
    @FXML private TextField subjectCodeField;

    private final SubjectApiService subjectApiService = new SubjectApiService();

    @FXML
    private void handleAddSubject() {
        String name = subjectNameField.getText();
        String code = subjectCodeField.getText();

        // Validate required fields
        if (name == null || name.isEmpty() || code == null || code.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Create JSON object with subject data
        JSONObject subjectData = new JSONObject();
        subjectData.put("name", name);
        subjectData.put("code", code);

        // Call API to add subject
        JSONObject response = subjectApiService.addSubject(subjectData);

        if (response != null) {
            showAlert("Success", "Subject added successfully.");
            clearForm(); // Clear form after successful submission
        } else {
            showAlert("Error", "Failed to add subject.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void clearForm() {
        subjectNameField.clear();
        subjectCodeField.clear();
    }
}

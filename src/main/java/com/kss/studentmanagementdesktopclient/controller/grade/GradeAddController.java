package com.kss.studentmanagementdesktopclient.controller.grade;

import com.kss.studentmanagementdesktopclient.api.GradeApiService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONObject;

public class GradeAddController {

    @FXML private TextField gradeValueField;
    @FXML private TextField subjectField;

    private final GradeApiService gradeApiService = new GradeApiService();

    @FXML
    private void handleAddGrade() {
        String gradeValue = gradeValueField.getText();
        String subject = subjectField.getText();

        // Validate required fields
        if (gradeValue == null || gradeValue.isEmpty() || subject == null || subject.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Create JSON object with grade data
        JSONObject gradeData = new JSONObject();
        gradeData.put("gradeValue", gradeValue);
        gradeData.put("subject", subject);

        // Call API to add grade
        JSONObject response = gradeApiService.addGrade(gradeData);

        if (response != null) {
            showAlert("Success", "Grade added successfully.");
            clearForm(); // Clear form after successful submission
        } else {
            showAlert("Error", "Failed to add grade.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void clearForm() {
        gradeValueField.clear();
        subjectField.clear();
    }
}

package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

/**
 * Controller for the Teacher Add view in the Student Management Desktop Client.
 * This JavaFX controller is used in the `teacher-add-view.fxml` file to handle user interactions
 * for adding a new teacher. It includes form validation, API calls, and form resetting.
 */
public class TeacherAddController {

    @FXML private TextField teacherNameField;

    private final TeacherApiService teacherApiService = new TeacherApiService();

    /**
     * Handles the action of adding a new teacher. Validates the input fields and,
     * upon successful validation, creates a JSON object with the teacher data and
     * sends it to the API. Displays appropriate success or error messages based on the API response.
     */
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

    /**
     * Displays an alert with the specified title and message.
     *
     * @param title   the title of the alert dialog
     * @param message the message content of the alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Clears the input fields in the form, resetting it for a new entry.
     */
    private void clearForm() {
        teacherNameField.clear();
    }
}

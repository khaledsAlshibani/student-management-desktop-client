package com.kss.studentmanagementdesktopclient.controller.subject;

import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Controller for the Subject Add view in the Student Management Desktop Client.
 * This JavaFX controller is used with `subject-add-view.fxml` to add a new subject,
 * including assigning a teacher from a dropdown list.
 */
public class SubjectAddController {

    @FXML private TextField subjectNameField;
    @FXML private ComboBox<String> teacherComboBox;

    private final SubjectApiService subjectApiService = new SubjectApiService();
    private final TeacherApiService teacherApiService = new TeacherApiService();

    /**
     * Initializes the add subject view by loading all available teachers
     * into the teacher dropdown list.
     */
    @FXML
    public void initialize() {
        loadTeachers();
    }

    /**
     * Loads all teachers from the API and populates the teacher dropdown (ComboBox).
     * Each item in the dropdown contains the teacher ID and name.
     * Displays an error alert if the data retrieval fails.
     */
    private void loadTeachers() {
        JSONArray teachers = teacherApiService.getAllTeachers();
        if (teachers != null) {
            for (int i = 0; i < teachers.length(); i++) {
                JSONObject teacher = teachers.getJSONObject(i);
                String teacherName = teacher.getString("name");
                Long teacherId = teacher.getLong("teacherId");
                teacherComboBox.getItems().add(teacherId + " - " + teacherName);
            }
        } else {
            showAlert("Error", "Failed to load teachers.");
        }
    }

    /**
     * Handles the action of adding a new subject. Validates required fields, extracts
     * the selected teacher ID, and sends the subject data to the API. Displays
     * appropriate success or error messages based on the response.
     */
    @FXML
    private void handleAddSubject() {
        String subjectName = subjectNameField.getText();
        String selectedTeacher = teacherComboBox.getValue();

        // Validate required fields
        if (subjectName == null || subjectName.isEmpty() || selectedTeacher == null || selectedTeacher.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Extract the teacher ID from the selected item
        Long teacherId = Long.parseLong(selectedTeacher.split(" - ")[0]);

        // Create JSON object with subject data
        JSONObject subjectData = new JSONObject();
        subjectData.put("name", subjectName);
        subjectData.put("teacherId", teacherId);

        // Call API to add subject
        JSONObject response = subjectApiService.addSubject(subjectData);

        if (response != null) {
            showAlert("Success", "Subject added successfully.");
            clearForm(); // Clear form after successful submission
        } else {
            showAlert("Error", "Failed to add subject.");
        }
    }

    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title   the title of the alert dialog
     * @param message the message content of the alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Clears the input fields in the form, resetting it for a new entry.
     */
    private void clearForm() {
        subjectNameField.clear();
        teacherComboBox.getSelectionModel().clearSelection();
    }
}

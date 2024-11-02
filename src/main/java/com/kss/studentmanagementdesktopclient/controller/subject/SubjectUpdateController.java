package com.kss.studentmanagementdesktopclient.controller.subject;

import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import com.kss.studentmanagementdesktopclient.app.DataReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONObject;

/**
 * Controller for the Subject Update view in the Student Management Desktop Client.
 * This JavaFX controller is used with `subject-update-view.fxml` to update or delete
 * an existing subject's details. It receives the subject ID from the previous scene.
 */
public class SubjectUpdateController implements DataReceiver {

    @FXML private TextField subjectNameField;
    @FXML private TextField subjectCodeField;

    private final SubjectApiService subjectApiService = new SubjectApiService();
    private Long subjectId;

    /**
     * Receives data from the previous scene. This method is used to pass the subject ID
     * to this controller so it can load and display the subject's details.
     *
     * @param data the data passed to this controller, expected to be of type Long representing the subject ID.
     */
    @Override
    public void setData(Object data) {
        if (data instanceof Long) {
            this.subjectId = (Long) data;
            loadSubjectDetails(subjectId);
        } else {
            System.err.println("Invalid data type passed to SubjectUpdateController.");
        }
    }

    /**
     * Loads the subject details from the API using the subject ID and displays the name
     * and code in the respective text fields. Shows an error alert if the data retrieval fails.
     *
     * @param subjectId the ID of the subject to retrieve details for.
     */
    private void loadSubjectDetails(Long subjectId) {
        JSONObject subjectData = subjectApiService.getSubjectById(subjectId);

        if (subjectData != null) {
            subjectNameField.setText(subjectData.getString("name"));
            subjectCodeField.setText(subjectData.getString("code"));
        } else {
            showAlert("Error", "Failed to load subject details.");
        }
    }

    /**
     * Handles the update action for the subject. Collects the updated subject name and code,
     * creates a JSON object with the data, and sends it to the API for updating.
     * Displays success or error messages based on the response.
     */
    @FXML
    private void handleUpdateSubject() {
        String name = subjectNameField.getText();
        String code = subjectCodeField.getText();

        if (name == null || name.isEmpty() || code == null || code.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        JSONObject subjectData = new JSONObject();
        subjectData.put("name", name);
        subjectData.put("code", code);

        JSONObject response = subjectApiService.updateSubject(subjectId, subjectData);

        if (response != null) {
            showAlert("Success", "Subject updated successfully.");
        } else {
            showAlert("Error", "Failed to update subject.");
        }
    }

    /**
     * Handles the delete action for the subject. Displays a confirmation dialog before
     * deletion. If confirmed, sends a delete request to the API. On successful deletion,
     * navigates back to the subject listing view.
     */
    @FXML
    private void handleDeleteSubject() {
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this subject?", ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Delete Subject");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean isDeleted = subjectApiService.deleteSubject(subjectId);
                if (isDeleted) {
                    showAlert("Success", "Subject deleted successfully.");
                    ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/subject/subject-listing-view.fxml", "Subjects Listing");
                } else {
                    showAlert("Error", "Failed to delete subject.");
                }
            }
        });
    }

    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title   the title of the alert dialog
     * @param message the message content of the alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

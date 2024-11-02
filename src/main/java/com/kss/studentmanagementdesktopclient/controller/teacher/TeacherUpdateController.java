package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.DataReceiver;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.json.JSONObject;

/**
 * Controller for the Teacher Update view in the Student Management Desktop Client.
 * This JavaFX controller is used with `teacher-update-view.fxml` to update or delete
 * an existing teacher's details. It receives the teacher ID from the previous scene.
 */
public class TeacherUpdateController implements DataReceiver {

    @FXML private TextField teacherNameField;

    private final TeacherApiService teacherApiService = new TeacherApiService();
    private Long teacherId;

    /**
     * Receives data from the previous scene. This method is used to pass the teacher ID
     * to this controller so it can load and display the teacher's details.
     *
     * @param data the data passed to this controller, expected to be of type Long representing the teacher ID.
     */
    @Override
    public void setData(Object data) {
        if (data instanceof Long) {
            this.teacherId = (Long) data;
            loadTeacherDetails();
        } else {
            System.err.println("Invalid data type passed to TeacherUpdateController.");
        }
    }

    /**
     * Loads the teacher details from the API using the teacher ID and displays the name
     * in the text field. Shows an error alert if the data retrieval fails.
     */
    private void loadTeacherDetails() {
        JSONObject teacherData = teacherApiService.getTeacherById(teacherId);

        if (teacherData != null) {
            teacherNameField.setText(teacherData.getString("name"));
        } else {
            showAlert("Error", "Failed to load teacher details.");
        }
    }

    /**
     * Handles the update action for the teacher. Collects the updated teacher name,
     * creates a JSON object with the data, and sends it to the API for updating.
     * Displays success or error messages based on the response.
     */
    @FXML
    private void handleUpdateTeacher() {
        try {
            String updatedName = teacherNameField.getText();

            JSONObject teacherData = new JSONObject();
            teacherData.put("name", updatedName);

            JSONObject response = teacherApiService.updateTeacher(teacherId, teacherData);

            if (response != null) {
                showAlert("Success", "Teacher updated successfully.");
            } else {
                showAlert("Error", "Failed to update teacher.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the teacher.");
        }
    }

    /**
     * Handles the delete action for the teacher. Displays a confirmation dialog before
     * deletion. If confirmed, sends a delete request to the API. On successful deletion,
     * navigates back to the teacher listing view.
     */
    @FXML
    private void handleDeleteTeacher() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Teacher");
        confirmAlert.setHeaderText("Are you sure you want to delete this teacher?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = teacherApiService.deleteTeacher(teacherId);

                if (isDeleted) {
                    showAlert("Success", "Teacher deleted successfully.");
                    ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/teacher/teacher-listing-view.fxml", "Teacher Listing");
                } else {
                    showAlert("Error", "Failed to delete teacher.");
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

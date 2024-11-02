package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.DataReceiver;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.json.JSONObject;

public class TeacherUpdateController implements DataReceiver {

    @FXML private TextField teacherNameField;

    private final TeacherApiService teacherApiService = new TeacherApiService();
    private Long teacherId;

    @Override
    public void setData(Object data) {
        if (data instanceof Long) {
            this.teacherId = (Long) data;
            loadTeacherDetails();
        } else {
            System.err.println("Invalid data type passed to TeacherUpdateController.");
        }
    }

    private void loadTeacherDetails() {
        JSONObject teacherData = teacherApiService.getTeacherById(teacherId);

        if (teacherData != null) {
            teacherNameField.setText(teacherData.getString("name"));
        } else {
            showAlert("Error", "Failed to load teacher details.");
        }
    }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
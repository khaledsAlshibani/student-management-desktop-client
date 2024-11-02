package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import com.kss.studentmanagementdesktopclient.app.DataReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentUpdateController implements DataReceiver {

    @FXML private TextField nameField;
    @FXML private TextField photoPathField;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextField birthplaceField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private RadioButton enrolledRadio;
    @FXML private RadioButton notEnrolledRadio;
    @FXML private VBox subjectsContainer;

    private final StudentApiService studentApiService = new StudentApiService();
    private ToggleGroup enrollmentStatusGroup;

    private Long studentId;

    @Override
    public void setData(Object data) {
        if (data instanceof Long) {
            this.studentId = (Long) data;
            loadStudentDetails(studentId);
        } else {
            System.err.println("Invalid data type passed to StudentUpdateController.");
        }
    }

    @FXML
    public void initialize() {
        // Setup ToggleGroup and other UI elements here
        enrollmentStatusGroup = new ToggleGroup();
        enrolledRadio.setToggleGroup(enrollmentStatusGroup);
        notEnrolledRadio.setToggleGroup(enrollmentStatusGroup);
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
        loadStudentDetails(studentId);
    }

    private void loadStudentDetails(Long studentId) {
        JSONObject studentData = studentApiService.getStudentById(studentId);

        if (studentData != null) {
            nameField.setText(studentData.getString("name"));
            photoPathField.setText(studentData.getString("photoUrl"));
            birthdatePicker.setValue(LocalDate.parse(studentData.getString("birthDate"), DateTimeFormatter.ISO_DATE));
            birthplaceField.setText(studentData.getString("birthPlace"));
            genderComboBox.setValue(studentData.getString("gender").substring(0, 1).toUpperCase() + studentData.getString("gender").substring(1));

            // Set enrollment status
            if (studentData.getString("enrollmentStatus").equalsIgnoreCase("enrolled")) {
                enrollmentStatusGroup.selectToggle(enrolledRadio);
            } else {
                enrollmentStatusGroup.selectToggle(notEnrolledRadio);
            }

            // Load subjects (check corresponding checkboxes)
            // Implement subject loading similarly to handleAddStudent
        } else {
            showAlert("Error", "Failed to load student details.");
        }
    }

    @FXML
    private void handleUpdateStudent() {
        try {
            JSONObject studentData = new JSONObject();
            studentData.put("name", nameField.getText());
            studentData.put("photoUrl", photoPathField.getText());
            studentData.put("birthDate", birthdatePicker.getValue().format(DateTimeFormatter.ISO_DATE));
            studentData.put("birthPlace", birthplaceField.getText());
            studentData.put("gender", genderComboBox.getValue().toLowerCase());

            RadioButton selectedStatus = (RadioButton) enrollmentStatusGroup.getSelectedToggle();
            studentData.put("enrollmentStatus", selectedStatus.getText().toLowerCase());

            // Collect subjects similarly to add functionality

            // Call API to update student
            JSONObject response = studentApiService.updateStudent(studentId, studentData);

            if (response != null) {
                showAlert("Success", "Student updated successfully.");
            } else {
                showAlert("Error", "Failed to update student.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteStudent() {
        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Student");
        confirmAlert.setHeaderText("Are you sure you want to delete this student?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean isDeleted = studentApiService.deleteStudent(studentId);

                    if (isDeleted) {
                        showAlert("Success", "Student deleted successfully.");
                        // Optional: Redirect to the listing view or close the update view
                    } else {
                        showAlert("Error", "Failed to delete student.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error", "An error occurred while trying to delete the student.");
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

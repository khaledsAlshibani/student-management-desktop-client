package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import com.kss.studentmanagementdesktopclient.app.DataReceiver;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the Student Update view in the Student Management Desktop Client.
 * This JavaFX controller is used with `student-update-view.fxml` to update or delete
 * an existing student's details. It receives the student ID from the previous scene.
 */
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

    /**
     * Receives data from the previous scene. This method is used to pass the student ID
     * to this controller so it can load and display the student's details.
     *
     * @param data the data passed to this controller, expected to be of type Long representing the student ID.
     */
    @Override
    public void setData(Object data) {
        if (data instanceof Long) {
            this.studentId = (Long) data;
            loadStudentDetails(studentId);
        } else {
            System.err.println("Invalid data type passed to StudentUpdateController.");
        }
    }

    /**
     * Initializes the update student view by setting up the enrollment status toggle group.
     */
    @FXML
    public void initialize() {
        enrollmentStatusGroup = new ToggleGroup();
        enrolledRadio.setToggleGroup(enrollmentStatusGroup);
        notEnrolledRadio.setToggleGroup(enrollmentStatusGroup);
    }

    /**
     * Loads the student details from the API using the student ID and displays the name,
     * photo URL, birthdate, birthplace, gender, and enrollment status in the respective fields.
     * Also loads the associated subjects for the student.
     *
     * @param studentId the ID of the student to retrieve details for.
     */
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

            // Load subjects (this section should be implemented to handle subject checkboxes)
            // Implement subject loading similarly to handleAddStudent if subjects are displayed as checkboxes
        } else {
            showAlert("Error", "Failed to load student details.");
        }
    }

    /**
     * Handles the update action for the student. Collects all updated fields, creates a JSON object
     * with the data, and sends it to the API for updating. Displays success or error messages based on the response.
     */
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
            showAlert("Error", "An error occurred while updating the student.");
        }
    }

    /**
     * Handles the delete action for the student. Displays a confirmation dialog before
     * deletion. If confirmed, sends a delete request to the API. On successful deletion,
     * navigates back to the student listing view.
     */
    @FXML
    private void handleDeleteStudent() {
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
                        ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/student/student-listing-view.fxml", "Student Listing");
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

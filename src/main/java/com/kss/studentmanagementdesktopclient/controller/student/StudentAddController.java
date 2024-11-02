package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Student Add view in the Student Management Desktop Client.
 * This JavaFX controller is used with `student-add-view.fxml` to add a new student,
 * including entering details such as name, photo path, birthdate, birthplace, gender,
 * enrollment status, and selected subjects.
 */
public class StudentAddController {

    @FXML private TextField nameField;
    @FXML private TextField photoPathField;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextField birthplaceField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private RadioButton enrolledRadio;
    @FXML private RadioButton notEnrolledRadio;
    @FXML private VBox subjectsContainer;

    private final StudentApiService studentApiService = new StudentApiService();
    private final SubjectApiService subjectApiService = new SubjectApiService();
    private ToggleGroup enrollmentStatusGroup;

    /**
     * Initializes the add student view by setting up the enrollment status toggle group,
     * gender options, and loading subjects into the UI.
     */
    @FXML
    public void initialize() {
        // Initialize ToggleGroup for enrollment status
        enrollmentStatusGroup = new ToggleGroup();
        enrolledRadio.setToggleGroup(enrollmentStatusGroup);
        notEnrolledRadio.setToggleGroup(enrollmentStatusGroup);

        // Populate gender options
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));

        // Fetch and populate subjects list
        loadSubjects();
    }

    /**
     * Loads all subjects from the API and displays them as checkboxes in the subjects container.
     * If no subjects are available, displays a message to prompt subject creation.
     */
    private void loadSubjects() {
        try {
            JSONArray subjectsArray = subjectApiService.getAllSubjects();
            if (subjectsArray.length() == 0) {
                Label noSubjectsLabel = new Label("First create a subject");
                subjectsContainer.getChildren().add(noSubjectsLabel);
            } else {
                subjectsContainer.getChildren().clear();
                for (int i = 0; i < subjectsArray.length(); i++) {
                    JSONObject subject = subjectsArray.getJSONObject(i);
                    String subjectName = subject.getString("name");
                    CheckBox subjectCheckBox = new CheckBox(subjectName);
                    subjectsContainer.getChildren().add(subjectCheckBox);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load subjects.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of adding a new student. Collects all form data, validates required fields,
     * and sends the data to the API. Displays success or error messages based on the response.
     */
    @FXML
    private void handleAddStudent() {
        try {
            // Collect form data
            String name = nameField.getText();
            String photoPath = photoPathField.getText();
            String birthdate = birthdatePicker.getValue() != null ? birthdatePicker.getValue().format(DateTimeFormatter.ISO_DATE) : null;
            String birthplace = birthplaceField.getText();
            String gender = genderComboBox.getValue();

            RadioButton selectedStatus = (RadioButton) enrollmentStatusGroup.getSelectedToggle();
            String enrollmentStatus = selectedStatus != null ? selectedStatus.getText().toLowerCase() : null;

            // Collect selected subjects
            List<String> selectedSubjects = new ArrayList<>();
            for (javafx.scene.Node node : subjectsContainer.getChildren()) {
                if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                    selectedSubjects.add(((CheckBox) node).getText());
                }
            }

            // Validate required fields
            if (name == null || name.isEmpty() ||
                    birthdate == null || birthplace == null || birthplace.isEmpty() ||
                    gender == null || enrollmentStatus == null) {
                showAlert("Validation Error", "Please fill in all required fields.");
                return;
            }

            // Create JSON object
            JSONObject studentData = new JSONObject();
            studentData.put("name", name);
            studentData.put("photoUrl", photoPath);
            studentData.put("birthDate", birthdate);
            studentData.put("birthPlace", birthplace);
            studentData.put("gender", gender.toLowerCase());
            studentData.put("enrollmentStatus", enrollmentStatus);

            // Add selected subjects as an array
            JSONArray subjectsArray = new JSONArray(selectedSubjects);
            studentData.put("subjects", subjectsArray);

            // Call API to add student
            JSONObject response = studentApiService.addStudent(studentData);

            if (response != null) {
                showAlert("Success", "Student added successfully.");
                clearForm(); // Clear form after successful submission
            } else {
                showAlert("Error", "Failed to add student.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Clears the input fields in the form, resetting it for a new entry.
     */
    private void clearForm() {
        nameField.clear();
        photoPathField.clear();
        birthdatePicker.setValue(null);
        birthplaceField.clear();
        genderComboBox.setValue(null);
        enrollmentStatusGroup.selectToggle(null);
        for (javafx.scene.Node node : subjectsContainer.getChildren()) {
            if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            }
        }
    }
}

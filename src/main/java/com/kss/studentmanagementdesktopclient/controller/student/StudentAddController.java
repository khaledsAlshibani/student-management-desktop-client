package com.kss.studentmanagementdesktopclient.controller.student;

import com.kss.studentmanagementdesktopclient.api.StudentApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentAddController {

    @FXML private TextField nameField;
    @FXML private TextField photoPathField;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextField birthplaceField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private RadioButton enrolledRadio;
    @FXML private RadioButton notEnrolledRadio;
    @FXML private ListView<CheckBox> subjectsListView;

    private final StudentApiService studentApiService = new StudentApiService();
    private ToggleGroup enrollmentStatusGroup;

    @FXML
    public void initialize() {
        // Initialize ToggleGroup for enrollment status
        enrollmentStatusGroup = new ToggleGroup();
        enrolledRadio.setToggleGroup(enrollmentStatusGroup);
        notEnrolledRadio.setToggleGroup(enrollmentStatusGroup);

        // Populate gender options
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));

        // Populate subjects list with sample data (replace this with real data)
        subjectsListView.setItems(FXCollections.observableArrayList(
                new CheckBox("Math"),
                new CheckBox("Science"),
                new CheckBox("History"),
                new CheckBox("Art")
        ));
    }

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
            for (CheckBox subjectCheckBox : subjectsListView.getItems()) {
                if (subjectCheckBox.isSelected()) {
                    selectedSubjects.add(subjectCheckBox.getText());
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        nameField.clear();
        photoPathField.clear();
        birthdatePicker.setValue(null);
        birthplaceField.clear();
        genderComboBox.setValue(null);
        enrollmentStatusGroup.selectToggle(null);
        for (CheckBox subjectCheckBox : subjectsListView.getItems()) {
            subjectCheckBox.setSelected(false);
        }
    }
}

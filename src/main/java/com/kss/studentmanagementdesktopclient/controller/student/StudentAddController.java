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

public class StudentAddController {

    @FXML private TextField nameField;
    @FXML private TextField photoPathField;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextField birthplaceField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private RadioButton enrolledRadio;
    @FXML private RadioButton notEnrolledRadio;
    @FXML private VBox subjectsContainer; // Replacing ListView with VBox

    private final StudentApiService studentApiService = new StudentApiService();
    private final SubjectApiService subjectApiService = new SubjectApiService();
    private ToggleGroup enrollmentStatusGroup;

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

    private void loadSubjects() {
        try {
            JSONArray subjectsArray = subjectApiService.getAllSubjects();
            if (subjectsArray.length() == 0) {
                Label noSubjectsLabel = new Label("First create a subject");
                subjectsContainer.getChildren().add(noSubjectsLabel);
            } else {
                subjectsContainer.getChildren().clear(); // Clear any existing content
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
        for (javafx.scene.Node node : subjectsContainer.getChildren()) {
            if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            }
        }
    }
}

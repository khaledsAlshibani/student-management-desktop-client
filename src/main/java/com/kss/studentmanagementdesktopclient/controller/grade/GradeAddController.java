package com.kss.studentmanagementdesktopclient.controller.grade;

import com.kss.studentmanagementdesktopclient.api.GradeApiService;
import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Grade Add view in the Student Management Desktop Client.
 * This JavaFX controller is used with `grade-add-view.fxml` to add a new grade,
 * allowing the user to enter a grade value and select associated subjects.
 */
public class GradeAddController {

    @FXML private TextField gradeValueField;
    @FXML private VBox subjectsContainer;

    private final List<CheckBox> subjectCheckboxes = new ArrayList<>();
    private final SubjectApiService subjectApiService = new SubjectApiService();

    /**
     * Initializes the add grade view by loading all available subjects
     * and displaying them as checkboxes in the subjects container.
     */
    @FXML
    private void initialize() {
        loadSubjects();
    }

    /**
     * Loads all subjects from the API and populates them as checkboxes in the subjects container.
     * If no subjects are available, displays a message prompting subject creation.
     */
    private void loadSubjects() {
        JSONArray subjects = subjectApiService.getAllSubjects();

        if (subjects == null || subjects.isEmpty()) {
            Label noSubjectsLabel = new Label("First create a subject");
            subjectsContainer.getChildren().add(noSubjectsLabel);
            return;
        }

        for (int i = 0; i < subjects.length(); i++) {
            JSONObject subject = subjects.getJSONObject(i);
            String subjectName = subject.getString("name");

            CheckBox checkBox = new CheckBox(subjectName);
            subjectCheckboxes.add(checkBox);
            subjectsContainer.getChildren().add(checkBox);
        }
    }

    /**
     * Handles the action of adding a new grade. Collects the grade value and selected subjects,
     * validates required fields, and sends the data to the API. Displays success or error messages
     * based on the response.
     */
    @FXML
    private void handleAddGrade() {
        String gradeValue = gradeValueField.getText();
        List<String> selectedSubjects = new ArrayList<>();

        for (CheckBox checkBox : subjectCheckboxes) {
            if (checkBox.isSelected()) {
                selectedSubjects.add(checkBox.getText());
            }
        }

        // Validation
        if (gradeValue == null || gradeValue.isEmpty() || selectedSubjects.isEmpty()) {
            showAlert("Validation Error", "Please enter a grade value and select at least one subject.");
            return;
        }

        // Prepare JSON data for grade
        JSONObject gradeData = new JSONObject();
        gradeData.put("gradeValue", gradeValue);
        gradeData.put("subjects", selectedSubjects);

        // Call API to add grade
        JSONObject response = new GradeApiService().addGrade(gradeData);

        if (response != null) {
            showAlert("Success", "Grade added successfully.");
            clearForm();
        } else {
            showAlert("Error", "Failed to add grade.");
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
        gradeValueField.clear();
        for (CheckBox checkBox : subjectCheckboxes) {
            checkBox.setSelected(false);
        }
    }
}

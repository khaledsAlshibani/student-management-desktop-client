package com.kss.studentmanagementdesktopclient.controller.grade;

import com.kss.studentmanagementdesktopclient.api.GradeApiService;
import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.Subject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Grade Add view in the Student Management Desktop Client.
 * This JavaFX controller is used with `grade-add-view.fxml` to add a new grade,
 * allowing the user to enter a grade value and select associated subjects.
 */
public class GradeListingController implements Initializable {
    @FXML
    private ListView<String> listView;

    private final GradeApiService gradeApiService = new GradeApiService();

    /**
     * Initializes the grade listing view by loading all grades into the list.
     * Adds an event listener for double-clicking on a grade entry to open the update view.
     *
     * @param location  the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGrades();

        // Add event listener for double-clicks to open grade update
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                String selectedGrade = listView.getSelectionModel().getSelectedItem();
                if (selectedGrade != null) {
                    openUpdateGradeView(selectedGrade);
                }
            }
        });
    }

    /**
     * Loads all grades from the API and displays them in the grade list.
     * Each entry in the list contains the grade ID and name.
     * Logs an error if a grade record is missing required fields or if no grades are found.
     */
    private void loadGrades() {
        JSONArray grades = gradeApiService.getAllGrades();

        if (grades != null) {
            for (int i = 0; i < grades.length(); i++) {
                JSONObject grade = grades.getJSONObject(i);

                // Check for the required fields
                if (!grade.has("gradeId") || !grade.has("score") || !grade.has("subject")) {
                    System.err.println("Geade record is missing required fields: " + grade.toString());
                    continue;
                }

                Long gradeId = grade.getLong("gradeId");
                Double score = grade.getDouble("score");

                listView.getItems().add("ID: " + gradeId + ", " + score);
            }
        } else {
            System.err.println("No grades found or failed to retrieve grade data.");
        }
    }

    /**
     * Opens the update view for the selected grade by passing the grade ID to the `grade-update-view.fxml` file.
     *
     * @param gradeInfo the selected grade info string from the list, which includes the grade ID.
     */
    private void openUpdateGradeView(String gradeInfo) {
        String gradeIdStr = gradeInfo.split(",")[0].split(":")[1].trim();
        Long gradeId = Long.parseLong(gradeIdStr);

        try {
            ViewManager.switchSceneWithData("/com/kss/studentmanagementdesktopclient/view/grade/grade-update-view.fxml", "Update Grade", gradeId);
        } catch (RuntimeException e) {
            System.err.println("Failed to open grade update form.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the add grade view, allowing the user to add a new grade to the system.
     * This action navigates to the `grade-add-view.fxml` file.
     */
    @FXML
    private void openAddTGrade() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/grade/grade-add-view.fxml", "Add Grade");
        } catch (RuntimeException e) {
            System.err.println("Failed to open add grade form.");
            e.printStackTrace();
        }
    }
}

package com.kss.studentmanagementdesktopclient.controller.subject;

import com.kss.studentmanagementdesktopclient.api.SubjectApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Subject Listing view in the Student Management Desktop Client.
 * This JavaFX controller is used with `subject-listing-view.fxml` to display a list of subjects,
 * handle double-click events to open the subject update form, and navigate to the add subject form.
 */
public class SubjectListingController implements Initializable {

    @FXML
    private ListView<String> subjectListView;
    @FXML
    private Label subjectsTitle;

    private final SubjectApiService subjectApiService = new SubjectApiService();

    /**
     * Initializes the subject listing view by loading all subjects into the list.
     * Adds an event listener for double-clicking on a subject entry to open the update view.
     *
     * @param location  the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSubjects();

        subjectListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                String selectedSubject = subjectListView.getSelectionModel().getSelectedItem();
                if (selectedSubject != null) {
                    openUpdateSubjectView(selectedSubject);
                }
            }
        });
    }

    /**
     * Loads all subjects from the API and displays them in the subject list.
     * Each entry in the list contains the subject ID and name. Sets the title label
     * with the total count of subjects retrieved. Logs an error if no subjects are found.
     */
    private void loadSubjects() {
        JSONArray subjects = subjectApiService.getAllSubjects();

        if (subjects != null) {
            for (int i = 0; i < subjects.length(); i++) {
                JSONObject subject = subjects.getJSONObject(i);

                if (!subject.has("subjectId") || !subject.has("name")) {
                    System.err.println("Subject record is missing required fields: " + subject.toString());
                    continue;
                }

                Long subjectId = subject.getLong("subjectId");
                String subjectName = subject.getString("name");

                subjectListView.getItems().add("ID: " + subjectId + ", " + subjectName);
            }

            subjectsTitle.setText("Subjects Listing (" + subjects.length() + ")");
        } else {
            System.err.println("No subjects found or failed to retrieve subject data.");
        }
    }

    /**
     * Opens the update view for the selected subject by passing the subject ID to the `subject-update-view.fxml` file.
     *
     * @param subjectInfo the selected subject info string from the list, which includes the subject ID.
     */
    private void openUpdateSubjectView(String subjectInfo) {
        String subjectIdStr = subjectInfo.split(",")[0].split(":")[1].trim();
        Long subjectId = Long.parseLong(subjectIdStr);

        try {
            ViewManager.switchSceneWithData("/com/kss/studentmanagementdesktopclient/view/subject/subject-update-view.fxml", "Update Subject", subjectId);
        } catch (RuntimeException e) {
            System.err.println("Failed to open subject update form.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the add subject view, allowing the user to add a new subject to the system.
     * This action navigates to the `subject-add-view.fxml` file.
     */
    @FXML
    private void openAddSubjectView() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/subject/subject-add-view.fxml", "Add Subject");
        } catch (RuntimeException e) {
            System.err.println("Failed to open add subject view.");
            e.printStackTrace();
        }
    }
}

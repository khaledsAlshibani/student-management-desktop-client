package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Teacher Listing view in the Student Management Desktop Client.
 * This JavaFX controller is used with `teacher-listing-view.fxml` to display a list of teachers,
 * handle double-click events to open the teacher update form, and navigate to the add teacher form.
 */
public class TeacherListingController implements Initializable {

    @FXML
    private ListView<String> teacherListView;

    private final TeacherApiService teacherApiService = new TeacherApiService();

    /**
     * Initializes the teacher listing view by loading all teachers into the list.
     * Adds an event listener for double-clicking on a teacher entry to open the update view.
     *
     * @param location  the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTeachers();

        // Add event listener for double-clicks to open teacher update
        teacherListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                String selectedTeacher = teacherListView.getSelectionModel().getSelectedItem();
                if (selectedTeacher != null) {
                    openUpdateTeacherView(selectedTeacher);
                }
            }
        });
    }

    /**
     * Loads all teachers from the API and displays them in the teacher list.
     * Each entry in the list contains the teacher ID and name.
     * Logs an error if a teacher record is missing required fields or if no teachers are found.
     */
    private void loadTeachers() {
        JSONArray teachers = teacherApiService.getAllTeachers();

        if (teachers != null) {
            for (int i = 0; i < teachers.length(); i++) {
                JSONObject teacher = teachers.getJSONObject(i);

                // Check for the required fields
                if (!teacher.has("teacherId") || !teacher.has("name")) {
                    System.err.println("Teacher record is missing required fields: " + teacher.toString());
                    continue;
                }

                Long teacherId = teacher.getLong("teacherId");
                String teacherName = teacher.getString("name");

                teacherListView.getItems().add("ID: " + teacherId + ", " + teacherName);
            }
        } else {
            System.err.println("No teachers found or failed to retrieve teacher data.");
        }
    }

    /**
     * Opens the update view for the selected teacher by passing the teacher ID to the `teacher-update-view.fxml` file.
     *
     * @param teacherInfo the selected teacher info string from the list, which includes the teacher ID.
     */
    private void openUpdateTeacherView(String teacherInfo) {
        String teacherIdStr = teacherInfo.split(",")[0].split(":")[1].trim();
        Long teacherId = Long.parseLong(teacherIdStr);

        try {
            ViewManager.switchSceneWithData("/com/kss/studentmanagementdesktopclient/view/teacher/teacher-update-view.fxml", "Update Teacher", teacherId);
        } catch (RuntimeException e) {
            System.err.println("Failed to open teacher update form.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the add teacher view, allowing the user to add a new teacher to the system.
     * This action navigates to the `teacher-add-view.fxml` file.
     */
    @FXML
    private void openAddTeacher() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/teacher/teacher-add-view.fxml", "Add Teacher");
        } catch (RuntimeException e) {
            System.err.println("Failed to open add teacher form.");
            e.printStackTrace();
        }
    }
}

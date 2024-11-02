package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherListingController implements Initializable {

    @FXML
    private ListView<String> teacherListView;

    private final TeacherApiService teacherApiService = new TeacherApiService();

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
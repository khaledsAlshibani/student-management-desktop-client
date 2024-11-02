package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
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
    }

    private void loadTeachers() {
        JSONArray teachers = teacherApiService.getAllTeachers();

        if (teachers != null) {
            for (int i = 0; i < teachers.length(); i++) {
                JSONObject teacher = teachers.getJSONObject(i);
                String teacherName = teacher.getString("name");

                // Check if subjects are available
                JSONArray subjectsArray = teacher.optJSONArray("subjects");
                String subjectsText = "";
                if (subjectsArray != null && subjectsArray.length() > 0) {
                    StringBuilder subjectsBuilder = new StringBuilder("Subjects: ");
                    for (int j = 0; j < subjectsArray.length(); j++) {
                        JSONObject subject = subjectsArray.getJSONObject(j);
                        subjectsBuilder.append(subject.getString("name"));
                        if (j < subjectsArray.length() - 1) {
                            subjectsBuilder.append(", ");
                        }
                    }
                    subjectsText = subjectsBuilder.toString();
                } else {
                    subjectsText = "No subjects assigned";
                }

                // Add teacher's name and subjects to the ListView
                teacherListView.getItems().add(teacherName + " - " + subjectsText);
            }
        }
    }

    @FXML
    private void openAddTeacher() {try {
        ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/teacher/teacher-add-view.fxml", "Teacher");
    } catch (RuntimeException e) {
        throw new RuntimeException(e);
    }
    }
}

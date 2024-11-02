package com.kss.studentmanagementdesktopclient.controller.teacher;

import com.kss.studentmanagementdesktopclient.api.TeacherApiService;
import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherListingController implements Initializable {

    @FXML
    private ListView<String> teacherListView;

    private final TeacherApiService teacherApiService = new TeacherApiService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTeachers(); // Load teachers when the view initializes

        // Set up click listener for ListView items
        teacherListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click to open the update page
                handleTeacherClick();
            }
        });
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

                // Add teacher's name and subjects to the ListView, including the teacher ID
                teacherListView.getItems().add(teacher.getLong("teacherId") + ":" + teacherName + " - " + subjectsText);
            }
        }
    }

    private void handleTeacherClick() {
        String selectedItem = teacherListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Long teacherId = Long.parseLong(selectedItem.split(":")[0]);  // Extract teacher ID from the selected item
            System.out.println("Clicked teacher ID: " + teacherId);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kss/studentmanagementdesktopclient/view/teacher/teacher-update-view.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the teacher ID
                TeacherUpdateController controller = loader.getController();
                controller.loadTeacher(teacherId); // Initialize with selected teacher data

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Teacher");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Selection Error", "Please select a teacher to update.");
        }
    }

    @FXML
    private void openAddTeacher() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/teacher/teacher-add-view.fxml", "Add Teacher");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

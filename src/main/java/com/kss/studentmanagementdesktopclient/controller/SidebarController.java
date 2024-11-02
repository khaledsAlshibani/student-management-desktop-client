package com.kss.studentmanagementdesktopclient.controller;

import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SidebarController {

    @FXML private Label dashboardLabel;
    @FXML private Label manageStudentsLabel;
    @FXML private Label manageTeachersLabel;
    @FXML private Label manageSubjectsLabel;
    @FXML private Label manageGradesLabel;

    @FXML
    private void initialize() {
        // Set up click handlers to navigate to different views
        dashboardLabel.setOnMouseClicked(event -> loadContent("dashboard"));
        manageStudentsLabel.setOnMouseClicked(event -> loadContent("manage_students"));
        manageTeachersLabel.setOnMouseClicked(event -> loadContent("manage_teachers"));
        manageSubjectsLabel.setOnMouseClicked(event -> loadContent("manage_subjects"));
        manageGradesLabel.setOnMouseClicked(event -> loadContent("manage_grades"));
    }

    private void loadContent(String view) {
        String fxmlPath;
        String title;

        switch (view) {
            case "dashboard":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/dashboard-view.fxml";
                title = "Dashboard";
                break;
            case "manage_students":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/student/student-listing-view.fxml";
                title = "Manage Students";
                break;
            case "manage_teachers":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/teacher/teacher-listing-view.fxml";
                title = "Manage Teachers";
                break;
            case "manage_subjects":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/subject/subject-add-view.fxml";
                title = "Manage Subjects";
                break;
            case "manage_grades":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/grade/grade-add-view.fxml";
                title = "Manage Grades";
                break;
            default:
                throw new IllegalArgumentException("Unknown view: " + view);
        }

        try {
            System.out.println("Loading view: " + view);
            ViewManager.switchScene(fxmlPath, title);
        } catch (RuntimeException e) {
            System.err.println("Failed to load view: " + view);
            e.printStackTrace();
        }
    }
}

package com.kss.studentmanagementdesktopclient.controller;

import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class AppController {

    @FXML private BorderPane container;
    @FXML private SidebarController sidebarController;

    @FXML
    private void initialize() {
        sidebarController.setOnNavigation(this::loadContent);
    }

    private void loadContent(String view) {
        String fxmlPath;
        String title;

        switch (view) {
            case "home":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/home-view.fxml";
                title = "Home";
                break;
            case "dashboard":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/dashboard.fxml";
                title = "Dashboard";
                break;
            case "manage_students":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/student/student-listing-view.fxml";
                title = "Student Listing";
                break;
            case "manage_teachers":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/teacher/manage_teachers.fxml";
                title = "Teachers";
                break;
            case "manage_subjects":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/subject/manage_subjects.fxml";
                title = "Subjects";
                break;
            case "manage_grades":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/grade/manage_grades.fxml";
                title = "Grades";
                break;
            default:
                throw new IllegalArgumentException("Unknown view: " + view);
        }

        try {
            System.out.println("loading view: " + view);
            ViewManager.switchScene(fxmlPath, title);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}

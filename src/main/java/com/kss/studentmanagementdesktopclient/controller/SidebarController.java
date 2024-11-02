package com.kss.studentmanagementdesktopclient.controller;

import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the sidebar navigation in the Student Management Desktop Client.
 * This controller is linked to the `sidebar.fxml` file and is responsible for handling
 * navigation between views for different sections, such as Dashboard, Manage Students,
 * Manage Teachers, Manage Subjects, and Manage Grades.
 */
public class SidebarController {

    @FXML private Label dashboardLabel;
    @FXML private Label manageStudentsLabel;
    @FXML private Label manageTeachersLabel;
    @FXML private Label manageSubjectsLabel;
    @FXML private Label manageGradesLabel;

    /**
     * Initializes the sidebar by setting up click handlers on each label to navigate to
     * the corresponding view. This method is automatically called after the `sidebar.fxml` file
     * is loaded, binding the UI elements to this controller.
     */
    @FXML
    private void initialize() {
        dashboardLabel.setOnMouseClicked(event -> loadContent("dashboard"));
        manageStudentsLabel.setOnMouseClicked(event -> loadContent("manage_students"));
        manageTeachersLabel.setOnMouseClicked(event -> loadContent("manage_teachers"));
        manageSubjectsLabel.setOnMouseClicked(event -> loadContent("manage_subjects"));
        manageGradesLabel.setOnMouseClicked(event -> loadContent("manage_grades"));
    }

    /**
     * Loads the specified view by switching the scene to the appropriate FXML file.
     *
     * @param view the identifier of the view to load. Accepted values are:
     *             "dashboard", "manage_students", "manage_teachers",
     *             "manage_subjects", and "manage_grades".
     * @throws IllegalArgumentException if an unknown view identifier is passed.
     */
    private void loadContent(String view) {
        String fxmlPath;
        String title;

        switch (view) {
            case "dashboard":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/home-view.fxml";
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
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/subject/subject-listing-view.fxml";
                title = "Manage Subjects";
                break;
            case "manage_grades":
                fxmlPath = "/com/kss/studentmanagementdesktopclient/view/grade/grade-listing-view.fxml";
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

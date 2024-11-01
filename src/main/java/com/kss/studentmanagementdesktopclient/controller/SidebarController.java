package com.kss.studentmanagementdesktopclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.function.Consumer;

public class SidebarController {

    @FXML private Label dashboardLabel;
    @FXML private Label manageStudentsLabel;
    @FXML private Label manageTeachersLabel;
    @FXML private Label manageSubjectsLabel;
    @FXML private Label manageGradesLabel;

    private Consumer<String> onNavigation;

    public void setOnNavigation(Consumer<String> onNavigation) {
        this.onNavigation = onNavigation;
    }

    @FXML
    private void initialize() {
        dashboardLabel.setOnMouseClicked(event -> navigate("dashboard"));
        manageStudentsLabel.setOnMouseClicked(event -> navigate("manage_students"));
        manageTeachersLabel.setOnMouseClicked(event -> navigate("manage_teachers"));
        manageSubjectsLabel.setOnMouseClicked(event -> navigate("manage_subjects"));
        manageGradesLabel.setOnMouseClicked(event -> navigate("manage_grades"));
    }

    private void navigate(String view) {
        if (onNavigation != null) {
            onNavigation.accept(view);
        }
    }
}
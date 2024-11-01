package com.kss.studentmanagementdesktopclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.util.function.Consumer;

public class HomeController {

    @FXML private Button manageStudentsButton;
    @FXML private Button manageTeachersButton;
    @FXML private Button manageSubjectsButton;
    @FXML private Button manageGradesButton;

    private Consumer<String> onNavigation;

    public void setOnNavigation(Consumer<String> onNavigation) {
        this.onNavigation = onNavigation;
    }

    @FXML
    private void initialize() {
        manageStudentsButton.setOnMouseClicked(event -> navigate("manage_students"));
        manageTeachersButton.setOnMouseClicked(event -> navigate("manage_teachers"));
        manageSubjectsButton.setOnMouseClicked(event -> navigate("manage_subjects"));
        manageGradesButton.setOnMouseClicked(event -> navigate("manage_grades"));
    }

    private void navigate(String view) {
        if (onNavigation != null) {
            onNavigation.accept(view);
        } else {
            System.err.println("Navigation callback is not set in HomeController.");
        }
    }
}

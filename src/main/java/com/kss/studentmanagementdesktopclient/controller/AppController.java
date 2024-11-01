package com.kss.studentmanagementdesktopclient.controller;

import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class AppController {

    @FXML
    private Button manageStudentsButton;

    @FXML
    private void initialize() {
        manageStudentsButton.setOnAction(event -> openStudentManagementView());
    }

    private void openStudentManagementView() {
        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/student/student-listing-view.fxml", "Home");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

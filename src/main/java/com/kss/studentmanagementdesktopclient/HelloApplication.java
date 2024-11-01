package com.kss.studentmanagementdesktopclient;

import app.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.init(stage);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/home-view.fxml", "Home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
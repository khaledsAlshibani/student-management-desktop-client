package com.kss.studentmanagementdesktopclient;

import com.kss.studentmanagementdesktopclient.app.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.init(stage);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        try {
            ViewManager.switchScene("/com/kss/studentmanagementdesktopclient/view/home-view.fxml", "Home");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
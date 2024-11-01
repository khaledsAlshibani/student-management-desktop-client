package com.kss.studentmanagementdesktopclient.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewManager {
    private static Stage primaryStage;

    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFilePath, String title) throws IOException {
        System.out.println("Loading FXML from: " + ViewManager.class.getResource(fxmlFilePath));
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlFilePath));
        Scene scene = new Scene(loader.load());

        try {
            scene.getStylesheets().add(ViewManager.class.getResource("/com/kss/studentmanagementdesktopclient/style/style.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading stylesheet.");
            e.printStackTrace();
        }

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
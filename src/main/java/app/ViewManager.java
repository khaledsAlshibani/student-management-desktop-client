package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewManager {
    private static Stage primaryStage;

    // Initialize with the primary stage
    public static void init(Stage stage) {
        primaryStage = stage;
    }

    // Load a new scene from an FXML file
    public static void switchScene(String fxmlFilePath, String title) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ViewManager.class.getResource(fxmlFilePath));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        try {
            scene.getStylesheets().add(ViewManager.class.getResource("/com/kss/studentmanagementdesktopclient/style/style.css").toExternalForm());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
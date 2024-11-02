package com.kss.studentmanagementdesktopclient.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * ViewManager is responsible for managing view transitions within the application.
 * It handles loading FXML files, setting the application stage, and passing data to
 * controllers when needed.
 */
public class ViewManager {
    private static Stage primaryStage;

    /**
     * Initializes the ViewManager with the primary stage of the application.
     *
     * @param stage the primary stage of the application
     */
    public static void init(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Switches the application scene to the specified FXML file without passing any data.
     *
     * @param fxmlFilePath the path to the FXML file to load
     * @param title        the title to set for the primary stage
     */
    public static void switchScene(String fxmlFilePath, String title) {
        try {
            System.out.println("Loading FXML from: " + ViewManager.class.getResource(fxmlFilePath));
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlFilePath));
            Scene scene = new Scene(loader.load());

            // Apply CSS stylesheet
            try {
                scene.getStylesheets().add(ViewManager.class.getResource("/com/kss/studentmanagementdesktopclient/style/style.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Error loading stylesheet.");
                e.printStackTrace();
            }

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFilePath);
            e.printStackTrace();
            showErrorDialog("Error", "Could not load the requested view.", "Please check the file path or contact support.");
        }
    }

    /**
     * Switches the application scene to the specified FXML file and passes data to the
     * controller if it implements the DataReceiver interface.
     *
     * @param fxmlFilePath the path to the FXML file to load
     * @param title        the title to set for the primary stage
     * @param data         the data to pass to the controller
     */
    public static void switchSceneWithData(String fxmlFilePath, String title, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlFilePath));
            Scene scene = new Scene(loader.load());

            // Apply CSS stylesheet
            try {
                scene.getStylesheets().add(ViewManager.class.getResource("/com/kss/studentmanagementdesktopclient/style/style.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Error loading stylesheet.");
                e.printStackTrace();
            }

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Pass data to the controller if it implements DataReceiver
            Object controller = loader.getController();
            if (controller == null) {
                System.err.println("Main controller is null.");
            } else {
                System.out.println("Main controller class: " + controller.getClass().getName());
            }
            if (controller instanceof DataReceiver) {
                ((DataReceiver) controller).setData(data);
            } else {
                System.err.println("Controller does not implement DataReceiver interface.");
            }
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFilePath);
            e.printStackTrace();
            showErrorDialog("Error", "Could not load the requested view.", "Please check the file path or contact support.");
        }
    }

    /**
     * Displays an error dialog with the specified title, header, and content message.
     *
     * @param title   the title of the error dialog
     * @param header  the header text of the error dialog
     * @param content the content text of the error dialog
     */
    private static void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

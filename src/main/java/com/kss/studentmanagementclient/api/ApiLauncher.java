package com.kss.studentmanagementclient.api;

import java.io.File;
import java.io.IOException;

public class ApiLauncher {

    private Process apiProcess;

    public void startApi() {
        try {
            // Specify the path to the Spring Boot API JAR
            File apiJar = new File("lib/student-management-api-0.0.1-SNAPSHOT.jar");

            // Start the API JAR as a separate process
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", apiJar.getAbsolutePath());
            apiProcess = builder.start();

            // Allow the API some time to start up before making requests
            Thread.sleep(5000); // Adjust if needed based on startup time

            System.out.println("API started successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Failed to start the API.");
        }
    }

    public void stopApi() {
        if (apiProcess != null) {
            apiProcess.destroy();
            System.out.println("API stopped.");
        }
    }
}

package com.kss.studentmanagementdesktopclient.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StudentController {

    @FXML
    private ListView<String> studentListView;

    @FXML
    public void initialize() {
        System.out.println("Controller Initialized"); // Debugging output
        fetchStudents();
    }

    private void fetchStudents() {
        try {
            System.out.println("Fetching students...");
            URL url = new URL("http://localhost:8080/api/students");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();

            // Parse JSON and add data to the ListView
            JSONArray studentsArray = new JSONArray(sb.toString());

            // Run on JavaFX Application Thread to update UI
            Platform.runLater(() -> {
                studentListView.getItems().clear(); // Clear existing items
                for (int i = 0; i < studentsArray.length(); i++) {
                    JSONObject student = studentsArray.getJSONObject(i);
                    String studentId = String.valueOf(student.getInt("studentId"));
                    String name = student.getString("name");
                    String birthPlace = student.getString("birthPlace");
                    String enrollmentStatus = student.getString("enrollmentStatus");

                    // Format the display text for each student
                    String studentInfo = String.format("ID: %s, Name: %s, Birth Place: %s, Status: %s",
                            studentId, name, birthPlace, enrollmentStatus);
                    studentListView.getItems().add(studentInfo);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

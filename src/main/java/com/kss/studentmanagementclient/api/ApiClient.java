package com.kss.studentmanagementclient.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/students";

    public static String getAllStudents() {
        return sendGetRequest(BASE_URL);
    }

    public static String getStudentById(Long id) {
        return sendGetRequest(BASE_URL + "/" + id);
    }

    public static String createStudent(String studentJson) {
        return sendPostRequest(BASE_URL, studentJson);
    }

    public static String updateStudent(Long id, String studentJson) {
        return sendPutRequest(BASE_URL + "/" + id, studentJson);
    }

    public static String deleteStudent(Long id) {
        return sendDeleteRequest(BASE_URL + "/" + id);
    }

    private static String sendGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return readResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to connect to API";
        }
    }

    private static String sendPostRequest(String urlString, String jsonInput) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            return readResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to connect to API";
        }
    }

    private static String sendPutRequest(String urlString, String jsonInput) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            return readResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to connect to API";
        }
    }

    private static String sendDeleteRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            return readResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to connect to API";
        }
    }

    private static String readResponse(HttpURLConnection connection) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}

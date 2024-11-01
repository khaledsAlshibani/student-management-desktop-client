package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class ApiService {

    protected abstract String getApiEndpoint();

    // Variable to store and log the selected URL source only once
    private static String apiBaseUrl = null;

    private String getApiFullUrl() {
        if (apiBaseUrl == null) {
            String localhostUrl = "http://localhost:8080/api";
            String onlineUrl = "https://student-management-api-production.up.railway.app/api";
            String endpoint = getApiEndpoint();

            // Test if the localhost URL is reachable
            if (isUrlReachable(localhostUrl + endpoint)) {
                apiBaseUrl = localhostUrl;
                System.out.println("API is running from localhost: " + apiBaseUrl + endpoint);
            } else {
                apiBaseUrl = onlineUrl;
                System.out.println("API is running from online server: " + apiBaseUrl + endpoint);
            }
        }
        return apiBaseUrl + getApiEndpoint();
    }

    private boolean isUrlReachable(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(2000); // Timeout in 2 seconds
            conn.connect();
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return false; // If an exception occurs, URL is not reachable
        }
    }

    public JSONArray fetchAll() {
        return (JSONArray) sendRequest("GET", null, getApiFullUrl());
    }

    public JSONObject fetchById(Long id) {
        return (JSONObject) sendRequest("GET", null, getApiFullUrl() + "/" + id);
    }

    public JSONObject create(JSONObject jsonData) {
        return (JSONObject) sendRequest("POST", jsonData, getApiFullUrl());
    }

    public JSONObject update(Long id, JSONObject jsonData) {
        return (JSONObject) sendRequest("PUT", jsonData, getApiFullUrl() + "/" + id);
    }

    public boolean delete(Long id) {
        return sendRequest("DELETE", null, getApiFullUrl() + "/" + id) == null; // Returns true if delete was successful
    }

    private Object sendRequest(String method, JSONObject jsonData, String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

            if (jsonData != null) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonData.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                conn.disconnect();

                // Determine if response is a JSONArray or JSONObject
                String responseText = response.toString();
                if (responseText.startsWith("[")) {
                    return new JSONArray(responseText);
                } else {
                    return new JSONObject(responseText);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                conn.disconnect();
                return null;
            } else {
                System.out.println("Request failed: " + responseCode);
                conn.disconnect();
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

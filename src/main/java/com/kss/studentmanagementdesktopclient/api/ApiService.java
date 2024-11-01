package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class ApiService {

    protected abstract String getApiUrl();

    public JSONArray fetchAll() {
        return (JSONArray) sendRequest("GET", null, getApiUrl());
    }

    public JSONObject fetchById(Long id) {
        return (JSONObject) sendRequest("GET", null, getApiUrl() + "/" + id);
    }

    public JSONObject create(JSONObject jsonData) {
        return (JSONObject) sendRequest("POST", jsonData, getApiUrl());
    }

    public JSONObject update(Long id, JSONObject jsonData) {
        return (JSONObject) sendRequest("PUT", jsonData, getApiUrl() + "/" + id);
    }

    public boolean delete(Long id) {
        return sendRequest("DELETE", null, getApiUrl() + "/" + id) == null; // Returns true if delete was successful
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

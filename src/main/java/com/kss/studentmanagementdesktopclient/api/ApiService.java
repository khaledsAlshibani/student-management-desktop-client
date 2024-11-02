package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Abstract class that provides base functionality for making HTTP requests to a REST API.
 * Subclasses should define the specific API endpoint by implementing {@link #getApiEndpoint()}.
 */
public abstract class ApiService {

    /**
     * Defines the specific API endpoint for the implementing subclass.
     *
     * @return the endpoint path specific to the subclass API.
     */
    protected abstract String getApiEndpoint();

    private static String apiBaseUrl = null; // Stores the selected base URL (localhost or online) for reuse

    /**
     * Constructs the full API URL based on the chosen base URL and endpoint.
     * If the base URL is not yet set, it tests reachability of localhost first, defaulting to the online URL otherwise.
     *
     * @return the full URL for the API request
     */
    private String getApiFullUrl() {
        if (apiBaseUrl == null) {
            String localhostUrl = "http://localhost:8080/api";
            String onlineUrl = "https://student-management-api-production.up.railway.app/api";
            String endpoint = getApiEndpoint();

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

    /**
     * Checks if a URL is reachable by sending a HEAD request.
     *
     * @param urlStr the URL to check for reachability
     * @return true if the URL is reachable, false otherwise
     */
    private boolean isUrlReachable(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(2000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Retrieves all records from the API.
     *
     * @return a JSONArray containing all records
     */
    public JSONArray fetchAll() {
        return (JSONArray) sendRequest("GET", null, getApiFullUrl());
    }

    /**
     * Retrieves a specific record by ID from the API.
     *
     * @param id the ID of the record to retrieve
     * @return a JSONObject containing the record details
     */
    public JSONObject fetchById(Long id) {
        return (JSONObject) sendRequest("GET", null, getApiFullUrl() + "/" + id);
    }

    /**
     * Creates a new record by sending a POST request with JSON data.
     *
     * @param jsonData the data to send in JSON format
     * @return a JSONObject containing the created record details
     */
    public JSONObject create(JSONObject jsonData) {
        return (JSONObject) sendRequest("POST", jsonData, getApiFullUrl());
    }

    /**
     * Updates an existing record by ID with the provided JSON data.
     *
     * @param id       the ID of the record to update
     * @param jsonData the data to update in JSON format
     * @return a JSONObject containing the updated record details
     */
    public JSONObject update(Long id, JSONObject jsonData) {
        return (JSONObject) sendRequest("PUT", jsonData, getApiFullUrl() + "/" + id);
    }

    /**
     * Deletes a record by ID from the API.
     *
     * @param id the ID of the record to delete
     * @return true if the delete was successful, false otherwise
     */
    public boolean delete(Long id) {
        return sendRequest("DELETE", null, getApiFullUrl() + "/" + id) == null;
    }

    /**
     * Sends an HTTP request to the specified URL with the specified method and JSON data.
     *
     * @param method   the HTTP method to use (GET, POST, PUT, DELETE)
     * @param jsonData the JSON data to send (null if no data)
     * @param urlStr   the URL to send the request to
     * @return an Object containing the response (JSONArray, JSONObject, or null)
     */
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
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                conn.disconnect();

                String responseText = response.toString();
                return responseText.startsWith("[") ? new JSONArray(responseText) : new JSONObject(responseText);
            } else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                conn.disconnect();
                return null;
            } else {
                System.out.println("Request failed: " + responseCode);
                conn.disconnect();
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }
}

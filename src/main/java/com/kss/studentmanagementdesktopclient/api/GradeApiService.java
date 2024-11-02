package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * GradeApiService provides methods to interact with the Grade API endpoint.
 * This service extends {@link ApiService} to perform CRUD operations specific to grades.
 */
public class GradeApiService extends ApiService {

    /**
     * Returns the API endpoint for grades.
     *
     * @return the endpoint path for grade-related operations
     */
    @Override
    protected String getApiEndpoint() {
        return "/grades";
    }

    /**
     * Retrieves all grades from the API.
     *
     * @return a JSONArray containing all grades
     */
    public JSONArray getAllGrades() {
        return fetchAll();
    }

    /**
     * Adds a new grade by sending a POST request with the specified grade data.
     *
     * @param gradeData the data for the new grade in JSON format
     * @return a JSONObject containing the created grade details, or null if the request fails
     */
    public JSONObject addGrade(JSONObject gradeData) {
        return create(gradeData);
    }
}

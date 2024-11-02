package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * SubjectApiService provides methods to interact with the Subject API endpoint.
 * This service extends {@link ApiService} to perform CRUD operations specific to subjects.
 */
public class SubjectApiService extends ApiService {

    /**
     * Returns the API endpoint for subjects.
     *
     * @return the endpoint path for subject-related operations
     */
    @Override
    protected String getApiEndpoint() {
        return "/subjects";
    }

    /**
     * Retrieves all subjects from the API.
     *
     * @return a JSONArray containing all subjects
     */
    public JSONArray getAllSubjects() {
        return fetchAll();
    }

    /**
     * Retrieves a specific subject by ID from the API.
     *
     * @param id the ID of the subject to retrieve
     * @return a JSONObject containing the subject's details
     */
    public JSONObject getSubjectById(Long id) {
        return fetchById(id);
    }

    /**
     * Adds a new subject by sending a POST request with the specified subject data.
     *
     * @param subjectData the data for the new subject in JSON format
     * @return a JSONObject containing the created subject's details, or null if the request fails
     */
    public JSONObject addSubject(JSONObject subjectData) {
        return create(subjectData);
    }

    /**
     * Updates a subject by ID with the specified subject data.
     *
     * @param id          the ID of the subject to update
     * @param subjectData the data to update in JSON format
     * @return a JSONObject containing the updated subject's details, or null if the request fails
     */
    public JSONObject updateSubject(Long id, JSONObject subjectData) {
        return update(id, subjectData);
    }

    /**
     * Deletes a subject by ID from the API.
     *
     * @param id the ID of the subject to delete
     * @return true if the delete was successful, false otherwise
     */
    public boolean deleteSubject(Long id) {
        return delete(id);
    }
}

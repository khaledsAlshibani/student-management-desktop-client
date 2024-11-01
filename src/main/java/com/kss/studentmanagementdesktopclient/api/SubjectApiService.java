package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

public class SubjectApiService extends ApiService {

    @Override
    protected String getApiEndpoint() {
        return "/subjects";
    }

    // Fetch all subjects (GET /api/subjects)
    public JSONArray getAllSubjects() {
        return fetchAll();
    }

    // Fetch a subject by ID (GET /api/subjects/{id})
    public JSONObject getSubjectById(Long id) {
        return fetchById(id);
    }

    // Create a new subject (POST /api/subjects)
    public JSONObject addSubject(JSONObject subjectData) {
        return create(subjectData);
    }

    // Update a subject by ID (PUT /api/subjects/{id})
    public JSONObject updateSubject(Long id, JSONObject subjectData) {
        return update(id, subjectData);
    }

    // Delete a subject by ID (DELETE /api/subjects/{id})
    public boolean deleteSubject(Long id) {
        return delete(id);
    }
}

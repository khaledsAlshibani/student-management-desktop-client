package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

public class GradeApiService extends ApiService {

    @Override
    protected String getApiEndpoint() {
        return "/grades";
    }

    // Fetch all grades (GET /api/grades)
    public JSONArray getAllGrades() {
        return fetchAll();
    }

    // Create a new grade (POST /api/grades)
    public JSONObject addGrade(JSONObject gradeData) {
        return create(gradeData);
    }
}

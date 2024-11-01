package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherApiService extends ApiService {

    @Override
    protected String getApiEndpoint() {
        return "/teachers";
    }

    // Fetch all teachers (GET /api/teachers)
    public JSONArray getAllTeachers() {
        return fetchAll();
    }

    // Fetch a teacher by ID (GET /api/teachers/{id})
    public JSONObject getTeacherById(Long id) {
        return fetchById(id);
    }

    // Create a new teacher (POST /api/teachers)
    public JSONObject addTeacher(JSONObject teacherData) {
        return create(teacherData);
    }

    // Update a teacher by ID (PUT /api/teachers/{id})
    public JSONObject updateTeacher(Long id, JSONObject teacherData) {
        return update(id, teacherData);
    }

    // Delete a teacher by ID (DELETE /api/teachers/{id})
    public boolean deleteTeacher(Long id) {
        return delete(id);
    }
}
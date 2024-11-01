package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

public class StudentApiService extends ApiService {

    @Override
    protected String getApiUrl() {
        return "https://student-management-api-production.up.railway.app/api/students";
    }

    // Fetch all students (GET /api/students)
    public JSONArray getAllStudents() {
        return fetchAll();
    }

    // Fetch a student by ID (GET /api/students/{id})
    public JSONObject getStudentById(Long id) {
        return fetchById(id);
    }

    // Create a new student (POST /api/students)
    public JSONObject addStudent(JSONObject studentData) {
        return create(studentData);
    }

    // Update a student by ID (PUT /api/students/{id})
    public JSONObject updateStudent(Long id, JSONObject studentData) {
        return update(id, studentData);
    }

    // Delete a student by ID (DELETE /api/students/{id})
    public boolean deleteStudent(Long id) {
        return delete(id);
    }
}

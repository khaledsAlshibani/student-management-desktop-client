package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * TeacherApiService provides methods to interact with the Teacher API endpoint.
 * This service extends {@link ApiService} to perform CRUD operations specific to teachers.
 */
public class TeacherApiService extends ApiService {

    /**
     * Returns the API endpoint for teachers.
     *
     * @return the endpoint path for teacher-related operations
     */
    @Override
    protected String getApiEndpoint() {
        return "/teachers";
    }

    /**
     * Retrieves all teachers from the API.
     *
     * @return a JSONArray containing all teachers
     */
    public JSONArray getAllTeachers() {
        return fetchAll();
    }

    /**
     * Retrieves a specific teacher by ID from the API.
     *
     * @param id the ID of the teacher to retrieve
     * @return a JSONObject containing the teacher's details
     */
    public JSONObject getTeacherById(Long id) {
        return fetchById(id);
    }

    /**
     * Adds a new teacher by sending a POST request with the specified teacher data.
     *
     * @param teacherData the data for the new teacher in JSON format
     * @return a JSONObject containing the created teacher's details, or null if the request fails
     */
    public JSONObject addTeacher(JSONObject teacherData) {
        return create(teacherData);
    }

    /**
     * Updates a teacher by ID with the specified teacher data.
     *
     * @param id          the ID of the teacher to update
     * @param teacherData the data to update in JSON format
     * @return a JSONObject containing the updated teacher's details, or null if the request fails
     */
    public JSONObject updateTeacher(Long id, JSONObject teacherData) {
        return update(id, teacherData);
    }

    /**
     * Deletes a teacher by ID from the API.
     *
     * @param id the ID of the teacher to delete
     * @return true if the delete was successful, false otherwise
     */
    public boolean deleteTeacher(Long id) {
        return delete(id);
    }
}

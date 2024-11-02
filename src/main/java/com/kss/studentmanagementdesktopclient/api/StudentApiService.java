package com.kss.studentmanagementdesktopclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * StudentApiService provides methods to interact with the Student API endpoint.
 * This service extends {@link ApiService} to perform CRUD operations specific to students.
 */
public class StudentApiService extends ApiService {

    /**
     * Returns the API endpoint for students.
     *
     * @return the endpoint path for student-related operations
     */
    @Override
    protected String getApiEndpoint() {
        return "/students";
    }

    /**
     * Retrieves all students from the API.
     *
     * @return a JSONArray containing all students
     */
    public JSONArray getAllStudents() {
        return fetchAll();
    }

    /**
     * Retrieves a specific student by ID from the API.
     *
     * @param id the ID of the student to retrieve
     * @return a JSONObject containing the student's details
     */
    public JSONObject getStudentById(Long id) {
        return fetchById(id);
    }

    /**
     * Adds a new student by sending a POST request with the specified student data.
     *
     * @param studentData the data for the new student in JSON format
     * @return a JSONObject containing the created student's details, or null if the request fails
     */
    public JSONObject addStudent(JSONObject studentData) {
        return create(studentData);
    }

    /**
     * Updates a student by ID with the specified student data.
     *
     * @param id          the ID of the student to update
     * @param studentData the data to update in JSON format
     * @return a JSONObject containing the updated student's details, or null if the request fails
     */
    public JSONObject updateStudent(Long id, JSONObject studentData) {
        return update(id, studentData);
    }

    /**
     * Deletes a student by ID from the API.
     *
     * @param id the ID of the student to delete
     * @return true if the delete was successful, false otherwise
     */
    public boolean deleteStudent(Long id) {
        return delete(id);
    }
}

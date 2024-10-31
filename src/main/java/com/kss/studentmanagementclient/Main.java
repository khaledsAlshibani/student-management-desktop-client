package com.kss.studentmanagementclient;

import com.kss.studentmanagementclient.api.ApiClient;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management Client");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            frame.add(scrollPane, BorderLayout.CENTER);

            JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));

            JButton getAllBtn = new JButton("Get All Students");
            getAllBtn.addActionListener(e -> {
                String response = ApiClient.getAllStudents();
                outputArea.setText(response);
            });

            JButton getByIdBtn = new JButton("Get Student By ID");
            getByIdBtn.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(frame, "Enter Student ID:");
                if (id != null && !id.isEmpty()) {
                    String response = ApiClient.getStudentById(Long.parseLong(id));
                    outputArea.setText(response);
                }
            });

            JButton createBtn = new JButton("Create Student");
            createBtn.addActionListener(e -> {
                String studentJson = "{\"name\":\"New Student\",\"birthDate\":\"2000-01-01\",\"birthPlace\":\"City\",\"gender\":\"Male\",\"enrollmentStatus\":\"Enrolled\",\"grades\":[85.5, 90.0],\"photoUrl\":\"http://example.com/photos/newstudent.jpg\"}";
                String response = ApiClient.createStudent(studentJson);
                outputArea.setText(response);
            });

            JButton updateBtn = new JButton("Update Student");
            updateBtn.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(frame, "Enter Student ID to update:");
                if (id != null && !id.isEmpty()) {
                    String studentJson = "{\"name\":\"Updated Student\",\"birthDate\":\"2000-01-01\",\"birthPlace\":\"City\",\"gender\":\"Male\",\"enrollmentStatus\":\"Graduated\",\"grades\":[95.0, 88.0],\"photoUrl\":\"http://example.com/photos/updatedstudent.jpg\"}";
                    String response = ApiClient.updateStudent(Long.parseLong(id), studentJson);
                    outputArea.setText(response);
                }
            });

            JButton deleteBtn = new JButton("Delete Student");
            deleteBtn.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(frame, "Enter Student ID to delete:");
                if (id != null && !id.isEmpty()) {
                    String response = ApiClient.deleteStudent(Long.parseLong(id));
                    outputArea.setText(response);
                }
            });

            panel.add(getAllBtn);
            panel.add(getByIdBtn);
            panel.add(createBtn);
            panel.add(updateBtn);
            panel.add(deleteBtn);

            frame.add(panel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}

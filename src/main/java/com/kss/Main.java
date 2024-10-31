package com.kss;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String message = ApiClient.getHelloWorld();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Management Client");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Center the window

            JLabel label = new JLabel(message, SwingConstants.CENTER);

            frame.add(label);
            frame.setVisible(true);
        });
    }
}
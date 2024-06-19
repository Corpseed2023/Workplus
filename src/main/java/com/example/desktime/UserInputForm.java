package com.example.desktime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class
UserInputForm extends JFrame {
    private JTextField usernameField;
    private JTextField existingUsernameField; // New field for existing username
    private JTextArea descriptionArea;

    public UserInputForm() {
        super("User Input Form");

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel existingUsernameLabel = new JLabel("Existing Username:"); // New label for existing username
        JLabel descriptionLabel = new JLabel("Description:");
        usernameField = new JTextField(20);
        existingUsernameField = new JTextField(20); // New text field for existing username
        descriptionArea = new JTextArea(5, 20);
        JButton submitButton = new JButton("Submit");

        // Set fonts and colors
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        usernameLabel.setFont(labelFont);
        existingUsernameLabel.setFont(labelFont); // Set font for existing username label
        descriptionLabel.setFont(labelFont);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        existingUsernameField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for existing username field
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(Color.BLUE);
        submitButton.setForeground(Color.WHITE);

        // Layout setup
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(5, 1)); // Changed to accommodate the new field
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(existingUsernameLabel); // Add existing username label
        formPanel.add(existingUsernameField); // Add existing username field
        formPanel.add(descriptionLabel);
        formPanel.add(new JScrollPane(descriptionArea));
        formPanel.add(submitButton);

        // Add ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String existingUsername = existingUsernameField.getText(); // Get existing username input
                String description = descriptionArea.getText();
                // Do something with the data, for example:
//                System.out.println("Username: " + username);
//                System.out.println("Existing Username: " + existingUsername); // Print existing username
//                System.out.println("Description: " + description);
            }
        });

        add(formPanel, BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserInputForm();
            }
        });
    }
}

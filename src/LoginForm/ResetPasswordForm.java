package Projet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ResetPasswordForm extends JFrame {

    private JTextField emailField; 
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private JButton backButton;
   
    public ResetPasswordForm() {
        super("Reset Password");
        setForeground(new Color(0, 0, 0));
        getContentPane().setForeground(new Color(0, 0, 0));
        getContentPane().setBackground(new Color(0, 102, 51));

        // Création des composants
        emailField = new JTextField(20); // Changement du nom de variable
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        resetButton = new JButton("Reset Password");
        resetButton.setForeground(new Color(0, 0, 51));
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        backButton = new JButton("Back");
        backButton.setForeground(new Color(0, 0, 51));
        backButton.setFont(new Font("Tahoma", Font.BOLD, 16));
         
        // Style personnalisé pour le formulaire de réinitialisation de mot de passe
        getContentPane().setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JLabel l1 = new JLabel("Reset Password");
        l1.setBackground(new Color(0, 0, 0));
        l1.setFont(new Font("Tahoma", Font.BOLD, 20));
        l1.setForeground(new Color(255, 255, 255));
        l1.setBounds(155, 11, 261, 40);
        JLabel emailLabel = new JLabel("Email:"); // Changement de texte du label
        emailLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        emailLabel.setForeground(new Color(255, 255, 255));
        emailLabel.setBounds(55, 86, 147, 40);
        JLabel pass = new JLabel("New Password:");
        pass.setFont(new Font("Tahoma", Font.BOLD, 16));
        pass.setForeground(new Color(255, 255, 255));
        pass.setBounds(55, 165, 147, 40);
        JLabel confirmPass = new JLabel("Confirm Password:");
        confirmPass.setFont(new Font("Tahoma", Font.BOLD, 16));
        confirmPass.setForeground(new Color(255, 255, 255));
        confirmPass.setBounds(55, 252, 186, 40);
        getContentPane().add(l1);
        getContentPane().add(emailLabel); // Ajout du label email
        getContentPane().add(pass);
        getContentPane().add(confirmPass);
        getContentPane().add(backButton); 
        
        emailField.setBounds(55, 126, 361, 40);
        passwordField.setBounds(55, 201, 361, 40);
        confirmPasswordField.setBounds(55, 292, 361, 40);
        resetButton.setBounds(113, 435, 250, 29);
        resetButton.setBackground(new Color(255, 255, 255));
        backButton.setBounds(113, 476, 250, 29);
        backButton.setBackground(new Color(255, 255, 255));


        getContentPane().add(emailField); // Changement du nom de variable
        getContentPane().add(passwordField);
        getContentPane().add(confirmPasswordField);
        getContentPane().add(resetButton);

        // Ajout d'un écouteur d'événements au bouton de réinitialisation du mot de passe
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText(); // Changement du nom de variable
                String newPassword = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Vérification si les mots de passe correspondent
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Passwords do not match", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Logique de réinitialisation du mot de passe
                resetPassword(email, newPassword);
            }
        });
        
        // Ajout d'un écouteur d'événements au bouton de retour en arrière
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme le formulaire actuel
                // Ouvrir le formulaire de connexion ou revenir à l'écran de connexion selon les besoins
                // Par exemple :
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
        
        // Configuration de la fenêtre
        setSize(493, 553);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour réinitialiser le mot de passe
    private void resetPassword(String email, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/events";
        String user = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, user, dbPassword)) {
            String sql = "UPDATE login SET password = ? WHERE email = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, newPassword);
                statement.setString(2, email);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Password reset successfully ");
                    dispose(); // Ferme la fenêtre de réinitialisation du mot de passe après la réinitialisation réussie
                } else {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Failed to reset password for email: " + email,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ResetPasswordForm.this, "An error occurred while resetting password", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ResetPasswordForm();
            }
        });
    }
}

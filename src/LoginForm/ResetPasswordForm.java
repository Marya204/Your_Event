package LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ResetPasswordForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton resetButton;
   private JButton backButton;
   
    public ResetPasswordForm() {
        super("Reset Password");
        setForeground(new Color(0, 0, 0));
        getContentPane().setForeground(new Color(0, 0, 0));
        getContentPane().setBackground(new Color(0, 102, 51));

        // Création des composants
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        emailField = new JTextField(20);
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
        JLabel name = new JLabel("Username:");
        name.setFont(new Font("Tahoma", Font.BOLD, 16));
        name.setForeground(new Color(255, 255, 255));
        name.setBounds(56, 86, 161, 40);
        JLabel pass = new JLabel("New Password:");
        pass.setFont(new Font("Tahoma", Font.BOLD, 16));
        pass.setForeground(new Color(255, 255, 255));
        pass.setBounds(55, 165, 147, 40);
        JLabel confirmPass = new JLabel("Confirm Password:");
        confirmPass.setFont(new Font("Tahoma", Font.BOLD, 16));
        confirmPass.setForeground(new Color(255, 255, 255));
        confirmPass.setBounds(55, 252, 147, 40);
        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Tahoma", Font.BOLD, 16));
        email.setForeground(new Color(255, 255, 255));
        email.setBounds(55, 333, 147, 40);
        getContentPane().add(l1);
        getContentPane().add(name);
        getContentPane().add(pass);
        getContentPane().add(confirmPass);
        getContentPane().add(email);
        getContentPane().add(backButton); 
        
        usernameField.setBounds(55, 126, 361, 40);
        passwordField.setBounds(55, 201, 361, 40);
        confirmPasswordField.setBounds(55, 292, 361, 40);
        emailField.setBounds(56, 372, 361, 40);
        resetButton.setBounds(113, 435, 250, 29);
        resetButton.setBackground(new Color(255, 255, 255));
        backButton.setBounds(113, 476, 250, 29);
        backButton.setBackground(new Color(255, 255, 255));


        getContentPane().add(usernameField);
        getContentPane().add(passwordField);
        getContentPane().add(confirmPasswordField);
        getContentPane().add(emailField);
        getContentPane().add(resetButton);

        // Ajout d'un écouteur d'événements au bouton de réinitialisation du mot de passe
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String newPassword = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();

                // Vérification si les mots de passe correspondent
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Passwords do not match", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Logique de vérification du compte par email
                if (!checkEmail(username, email)) {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Username and email do not match", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Logique de réinitialisation du mot de passe
                resetPassword(username, newPassword);
            }
        });
        
               // Ajout d'un écouteur d'événements au bouton de retour en arrière
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current form
                // Open the login form or navigate back to the login screen as needed
                // For example:
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
        
        // Configuration de la fenêtre
        setSize(493, 553);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour vérifier si l'adresse e-mail correspond au nom d'utilisateur dans la base de données
    private boolean checkEmail(String username, String email) {
        String url = "jdbc:mysql://localhost:3306/your_event";
        String user = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, user, dbPassword)) {
            String sql = "SELECT * FROM login WHERE username = ? AND email = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // Si une ligne correspondante est trouvée, retourne vrai
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ResetPasswordForm.this, "An error occurred while checking email", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Méthode pour réinitialiser le mot de passe
    private void resetPassword(String username, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/your_event";
        String user = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, user, dbPassword)) {
            String sql = "UPDATE login SET password = ? WHERE username = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, newPassword);
                statement.setString(2, username);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Password reset successful for user: " + username);
                    dispose(); // Ferme la fenêtre de réinitialisation du mot de passe après la réinitialisation réussie
                } else {
                    JOptionPane.showMessageDialog(ResetPasswordForm.this, "Failed to reset password for user: " + username,
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
        new ResetPasswordForm();
    }
}



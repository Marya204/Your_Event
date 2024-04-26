package LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        super("Login");
        setForeground(new Color(0, 0, 0));
        getContentPane().setForeground(new Color(0, 0, 0));
        getContentPane().setBackground(new Color(0, 102, 51));

        // Création des composants
        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(235, 219, 204));
        usernameField.setForeground(new Color(0, 0, 0));
        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(235, 219, 204));
        loginButton = new JButton("Login");
        loginButton.setForeground(new Color(0, 0, 0));
        loginButton.setBackground(new Color(235, 219, 204));
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 16));

        // Style personnalisé pour le formulaire de connexion
        getContentPane().setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel l1 = new JLabel("Login");
        l1.setBackground(new Color(0, 0, 0));
        l1.setFont(new Font("Tahoma", Font.BOLD, 30));
        l1.setForeground(new Color(235, 219, 204));
        l1.setBounds(133, 22, 104, 40);
        JLabel name = new JLabel("Username:");
        name.setBackground(new Color(235, 219, 204));
        name.setFont(new Font("Tahoma", Font.BOLD, 16));
        name.setForeground(new Color(235, 219, 204));
        name.setBounds(40, 82, 147, 40);
        JLabel pass = new JLabel("Password:");
        pass.setFont(new Font("Tahoma", Font.BOLD, 16));
        pass.setForeground(new Color(235, 219, 204));
        pass.setBounds(45, 207, 161, 40);
        getContentPane().add(l1);
        getContentPane().add(name);
        getContentPane().add(pass);
        usernameField.setBounds(40, 133, 283, 51);
        passwordField.setBounds(40, 258, 283, 51);
        loginButton.setBounds(94, 365, 174, 29);
        loginButton.setBackground(new Color(235, 219, 204));

        getContentPane().add(usernameField);
        getContentPane().add(passwordField);
        getContentPane().add(loginButton);

        // Ajout d'un écouteur d'événements au bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validation des informations d'identification
                if (authenticateUser(username, password)) {
                    dispose(); // Fermez la fenêtre de connexion
                    new Dashboard();
                    // Redirection après authentification réussie
                    // Vous pouvez rediriger l'utilisateur vers la page principale de l'application
                    // ou ouvrir une nouvelle fenêtre
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid username or password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Création d'un JLabel avec du texte cliquable pour "mot de passe oublié"
        JLabel forgotPasswordLabel = new JLabel("Forgot your password?");
        forgotPasswordLabel.setForeground(new Color(235, 219, 204));
        forgotPasswordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.setBounds(94, 421, 189, 23);
        getContentPane().add(forgotPasswordLabel);
        
        ImageIcon imageIcon = new ImageIcon("/IMAGE/yedesign2.png");
        JLabel imageLabel = new JLabel(new ImageIcon("C:\\Users\\hp\\Downloads\\yedesigne2.png"));
        imageLabel.setBounds(379, 0, 374, 455);
        getContentPane().add(imageLabel);


        // Ajout d'un écouteur d'événements pour le texte cliquable "mot de passe oublié"
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	dispose();
                // Ouvre la page de réinitialisation de mot de passe lorsque le label est cliqué
                new ResetPasswordForm();
            }
        });

        // Configuration de la fenêtre
        setSize(767, 492);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour réinitialiser le mot de passe (vous devez implémenter cette méthode selon votre logique)
    private void resetPassword(String username) {
        System.out.println("Resetting password for user: " + username);
        JOptionPane.showMessageDialog(LoginForm.this, "Password reset functionality will be implemented here!");
    }

    private boolean authenticateUser(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/your_event";
        String user = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, user, dbPassword)) {
            String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password); // Ne pas hacher le mot de passe
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // true si l'utilisateur existe
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    

    public static void main(String[] args) {
    	 SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 new LoginForm();
             }
         });
}
}

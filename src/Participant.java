package Projet;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import Projet.Event;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Participant extends JPanel {
	private JPanel mainPanel;
    private JTextField searchField;
    private JDialog addEventDialog;
    private JTable table;
    private DefaultTableModel tableModel;

    public Participant() {
        setLayout(new BorderLayout());

        // Initialize main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Utiliser un BoxLayout vertical
        // Add a marge in the top of the page
        mainPanel.add(Box.createVerticalStrut(20));
        // Add title "Participants" to the main panel
        JLabel titleLabelEvents = new JLabel("PARTICIPANTS", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents, BorderLayout.NORTH);

        // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // Left-aligned flow layout
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        searchPanel.setBackground(new Color(235, 235, 235)); // Set background color

        // Create the search field with placeholder text
        searchField = new PlaceholderTextField("Search for a participant");
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(60, 165, 92))); // Set border color
        searchField.setBackground(Color.WHITE); // Set background color
        searchField.setForeground(Color.BLACK); // Set foreground color
        searchPanel.add(searchField);
        
        // Ajouter un écouteur sur le champ de recherche
        searchField.getDocument().addDocumentListener(new DocumentListener() {
        
            public void insertUpdate(DocumentEvent e) {
            	filterParticipants();
            }

            public void removeUpdate(DocumentEvent e) {
            	filterParticipants();
            }
            public void changedUpdate(DocumentEvent e) {
            	filterParticipants();
            }
        });
        // Add space between components
        searchPanel.add(Box.createHorizontalStrut(300)); // Add space between search field and "Add Event" button

        // Create "Add Event" button with icon
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\hp\\\\Downloads\\\\Image\\\\Image\\\\plus.png"); // Change to your icon file path
        JButton addButton = new JButton("Add participant", plusIcon);
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.setBackground(new Color(60, 165, 92)); // Set background color
        addButton.setForeground(new Color(235, 219, 204)); // Set foreground color
        addButton.setFocusPainted(false); // Remove focus border
        searchPanel.add(addButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH); // Add search panel to main panel

        // Modifier l'actionListener du bouton "Add Event" pour afficher le formulaire
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddParticipantForm(); // Afficher le formulaire d'ajout d'événement
            }
        });
        
        
        
        
        // Add table to the main panel
        String[] columns = { "Participant ID", "Name", "Email", "Event ID" };
        Object[][] data = getParticipantDataFromDatabase();
        tableModel = new DefaultTableModel(data, columns);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 165, 92));
        table.getTableHeader().setForeground(new Color(235, 219, 204));
        table.setSelectionBackground(new Color(181, 172, 73));
        table.setSelectionForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
     // Create a "Modify" button
        JButton modifyButton = new JButton("Modify Selected");
        modifyButton.setPreferredSize(new Dimension(200, 40));
        modifyButton.setBackground(new Color(60, 165, 92)); // Green background
        modifyButton.setForeground(new Color(235, 219, 204)); // White text
        modifyButton.setFocusPainted(false); // Remove focus border
        // Add action listener to the "Modify" button
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the event data from the selected row
               
                	int ParticipantID = (int) table.getValueAt(selectedRow, 0);

                    String Name = (String) table.getValueAt(selectedRow, 1);
                    String Email = (String) table.getValueAt(selectedRow, 2);
                   
                    int Eventid = (int) table.getValueAt(selectedRow, 0);
                    // Open modification dialog with pre-filled data
                    openModifyparticipantDialog(ParticipantID,Name,Email,Eventid);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to modify.");
                }
            }
        });
        // Create remove button
        JButton removeButton = new JButton("Remove Selected");
        removeButton.setPreferredSize(new Dimension(200, 40));
        removeButton.setBackground(new Color(60, 165, 92)); // Green background
        removeButton.setForeground(new Color(235, 219, 204)); // White text
        removeButton.setFocusPainted(false); // Remove focus border
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the event ID from the selected row
                    int ParticipantID = (int) table.getValueAt(selectedRow, 0);
                    // Remove the row from the table model
                    tableModel.removeRow(selectedRow);
                    // Delete the event from the database
                    deleteParticipantFromDatabase(ParticipantID);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to remove.");
                }
            }
        });
        
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setBackground(new Color(60, 165, 92)); // Green background
        backButton.setForeground(new Color(255,255,255)); // White text
        backButton.setFocusPainted(false); // Remove focus border
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Window window = SwingUtilities.getWindowAncestor(Participant.this);
                window.dispose();
               Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });
        // Add the "Modify" button to a panel
        JPanel modifyPanel = new JPanel();
        modifyPanel.setBackground(Color.WHITE); // Set background color
        modifyPanel.add(modifyButton); // Add modify button to the panel
        // Add the modify panel to the main panel's SOUTH position
        mainPanel.add(modifyPanel, BorderLayout.SOUTH);
     
        // Create a panel for the remove button
        JPanel removePanel = new JPanel();
        removePanel.setBackground(Color.WHITE); // Set background color
        removePanel.add(removeButton); // Add remove button to the panel

        // Add the remove panel to the main panel's SOUTH position
        mainPanel.add(removePanel, BorderLayout.SOUTH);
     // Add the "Modify" button to the panel with the table
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(modifyButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
    private void openModifyparticipantDialog(int ParticipantID, String Name, String Email, int Eventid) {
        JDialog modifyDialog = new JDialog();
        modifyDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        modifyDialog.getContentPane().setBackground(Color.white); // Green background
        modifyDialog.getContentPane().setForeground(Color.black); // White text
        modifyDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel = createLabel("Modify Participant", new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        modifyDialog.add(titleLabel, gbc);

        // Create text fields with the same style as the addition form
        JTextField nameTextField = new JTextField();
        JTextField emailTextField = new JTextField();
        JTextField EventidTextField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(nameTextField, "Enter Name...");
        setTextFieldStyle(emailTextField, "Enter Email...");
        setTextFieldStyle(EventidTextField, "Enter Event ID...");

        // Set the text of the text fields to the provided values
        nameTextField.setText(Name);
        emailTextField.setText(Email);
        EventidTextField.setText(String.valueOf(Eventid));

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Name:", nameTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Email:", emailTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Event ID:", EventidTextField);

        // Create the "Save" button
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 165, 92)); // Green background
        saveButton.setForeground(new Color(235, 219, 204)); // White text
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        modifyDialog.add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get modified data from the fields
                String newName = nameTextField.getText();
                String newEmail = emailTextField.getText();
                int newEventid = Integer.parseInt(EventidTextField.getText());
                // Update event data in the table
                int selectedRow = table.getSelectedRow();
                table.setValueAt(newName, selectedRow, 1);
                table.setValueAt(newEmail, selectedRow, 2);
                table.setValueAt(newEventid, selectedRow, 3);
                // Update event data in the database
                updateParticipantInDatabase(ParticipantID, newName, newEmail, newEventid);

                // Close the modification dialog
                modifyDialog.dispose();
            }
        });
        // Set dialog properties
        modifyDialog.setSize(400, 400);
        modifyDialog.setLocationRelativeTo(this);
        modifyDialog.setVisible(true);
    }



 // Method to update event data in the database
    private void updateParticipantInDatabase(int participantID, String Name, String Email, int Eventid) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "UPDATE participant SET Name = ?, Email = ?, Eventid = ? WHERE ParticipantID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Name);
            statement.setString(2, Email);
            statement.setInt(3, Eventid); // Utilisation de setInt pour Eventid
            statement.setInt(4, participantID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Participant mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour du participant !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteParticipantFromDatabase(int ParticipantID) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL DELETE query
            String query = "DELETE FROM participant WHERE ParticipantID = ?";
            
            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setLong(1, ParticipantID);

            // Execute the deletion query
            int rowsDeleted = statement.executeUpdate();
            
            // Check if deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("participant deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle connection or query execution errors
        }
    }
    
   
    
    private Object[][] getParticipantDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM participant";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Object[]> participantData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[4];
                row[0] = resultSet.getInt("ParticipantID"); // Correct column name
                row[1] = resultSet.getString("Name"); // Correct column name
                row[2] = resultSet.getString("Email");
                row[3] = resultSet.getInt("Eventid");
                participantData.add(row);
            }

            Object[][] data = new Object[participantData.size()][];
            for (int i = 0; i < participantData.size(); i++) {
                data[i] = participantData.get(i);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    private void setTextFieldStyle(JTextField textField, String placeholder) {
        textField.setFont(new Font("Arial", Font.BOLD, 14)); // Set font and size
        textField.setText(placeholder);
        textField.setForeground(new Color(128, 128, 128)); // Set placeholder color
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 165, 92), 2), // Set border color and thickness
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Set padding
        ));
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.black); // Set regular text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(235, 219, 204)); // Set placeholder color
                }
            }
        });
        
    }
    // Helper method to create a JLabel with specified text and color
    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }
    // Helper method to add a field with label to the modification dialog
    private void addFieldWithLabel(GridBagConstraints gbc, Container container, String labelText, JTextField textField) {
        JLabel label = createLabel(labelText, Color.black);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        container.add(label, gbc);
        gbc.gridx++;
        container.add(textField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void createAddParticipantForm() {
        addEventDialog = new JDialog();
        addEventDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
      
        // Apply the same background color and font as the main panel
        addEventDialog.getContentPane().setBackground(Color.white); // Green background
        addEventDialog.getContentPane().setForeground(Color.black); // White text
        addEventDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Add the title label
        JLabel titleLabel = createLabel("Add Participant",new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        addEventDialog.add(titleLabel, gbc);

        // Create text fields with white background and black text
        JTextField NameField = new JTextField();
        JTextField EmailField = new JTextField();
        JTextField EventIDField = new JTextField();

        // Call setTextFieldStyle for each text field
        setTextFieldStyle(NameField, "Enter Name...");
        setTextFieldStyle(EmailField, "Enter Email...");
        setTextFieldStyle(EventIDField, "Enter Event ID...");

        // Apply the same background and foreground colors as the main panel
        NameField.setBackground(Color.WHITE);
        NameField.setForeground(Color.BLACK);
        EmailField.setBackground(Color.WHITE);
        EmailField.setForeground(Color.BLACK);
        

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Name:", NameField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Email:", EmailField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Event ID:", EventIDField);
      

        // Add a button to validate the event addition
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(60, 165, 92)); // Green background
        addButton.setForeground(new Color(235, 219, 204)); // White text
        addButton.setFocusPainted(false); // Remove focus border
        addButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        addEventDialog.add(addButton, gbc);

        // Add action listener to the "Add" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the data entered in the form
                String Name = NameField.getText();
                String Email = EmailField.getText();
             
                int EventID = Integer.parseInt(EventIDField.getText());

                // Add the event to the database
                addParticipantToDatabase(Name, Email,EventID);

                // Close the form after addition
                addEventDialog.dispose();
            }
        });

        // Set the properties of the JDialog
        addEventDialog.setSize(400, 400);
        addEventDialog.setLocationRelativeTo(this);
        addEventDialog.setVisible(true);
    }
 // Ajouter une méthode pour ajouter un événement à la base de données
    private void addParticipantToDatabase(String Name, String Email,int Eventid) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Créer la requête SQL d'insertion
            String query = "INSERT INTO participant (Name, Email,EventID) " +
                           "VALUES (?, ?,?)";
            
            // Préparer la déclaration SQL
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Définir les valeurs des paramètres dans la requête SQL
            statement.setString(1, Name);
            statement.setString(2, Email);
            statement.setLong(3, Eventid);
            
            // Exécuter la requête d'insertion
            int rowsInserted = statement.executeUpdate();
            
            // Vérifier si l'insertion a réussi
            if (rowsInserted > 0) {
                System.out.println("Le participant a été ajouté avec succès !");
                // Actualiser l'affichage si nécessaire
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les erreurs de connexion ou d'exécution de la requête
        }
    }

    

    private void filterParticipants() {
        // Récupérer le texte saisi par l'utilisateur
        String Name = searchField.getText().trim().toLowerCase();

        // Récupérer les données depuis la base de données en fonction du Name filtré
        Object[][] filteredData = getparticipantDataFromDatabase(Name);

        // Utiliser directement la variable de table au niveau de la classe
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();

        model.setRowCount(0); // Effacer les lignes existantes
        for (Object[] row : filteredData) {
            model.addRow(row); // Ajouter les lignes filtrées
        }
    }
    
    private Object[][] getparticipantDataFromDatabase(String Name) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM participant"; // Récupérer les données de la table 'participant'

            // Ajout de la clause WHERE si un nom est spécifié
            if (Name != null && !Name.isEmpty()) {
                query += " WHERE Name LIKE ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);

            // Si un Nom est spécifié, définissez le paramètre dans la requête
            if (Name != null && !Name.isEmpty()) {
                statement.setString(1, "%" + Name + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            // Traitement des résultats et création des données filtrées
            List<Object[]> participantData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[4];
                row[0] = resultSet.getInt("ParticipantID");
                row[1] = resultSet.getString("Name");
                row[2] = resultSet.getString("Email");
                row[3] = resultSet.getInt("Eventid"); // Utilisation de getInt pour Eventid
                participantData.add(row);
            }

            // Conversion de la liste en tableau à deux dimensions
            Object[][] data = new Object[participantData.size()][];
            for (int i = 0; i < participantData.size(); i++) {
                data[i] = participantData.get(i);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }
    // Helper method to create custom buttons
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2d = (Graphics2D) g.create();
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, bgColor, w, h, Color.WHITE); // Gradient from bgColor to
                                                                                        // white
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };

        button.setForeground(new Color(235, 219, 204));
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
    


    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Participants Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Participant ParticipantContentPanel = new Participant();
                frame.add(ParticipantContentPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

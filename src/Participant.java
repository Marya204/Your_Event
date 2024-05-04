package Projet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Projet.Event;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Invite extends JPanel {
    private JTextField searchField;
    private JButton addButton, removeButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public Invite() {
        setLayout(new BorderLayout());
        
        // Initialize mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Use a vertical BoxLayout
        // Add a marge in the top of the page
        mainPanel.add(Box.createVerticalStrut(20));
        // Add title "Invités" to the main panel
        JLabel titleLabelEvents = new JLabel("GUESTS", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents, BorderLayout.NORTH);

        // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // Left-aligned flow layout
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        searchPanel.setBackground(new Color(235, 235, 235)); // Set background color

        // Create the search field with placeholder text
        searchField = new PlaceholderTextField("Search for a guest");
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(60, 165, 92))); // Set border color
        searchField.setBackground(Color.WHITE); // Set background color
        searchField.setForeground(Color.BLACK); // Set foreground color
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterInvites();
            }

            public void removeUpdate(DocumentEvent e) {
                filterInvites();
            }

            public void changedUpdate(DocumentEvent e) {
                filterInvites();
            }
        });
        searchPanel.add(Box.createHorizontalStrut(300)); // Add space between search field and "Add Event" button

        // Create "Add Invite" button
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\hp\\\\Downloads\\\\Image\\\\Image\\\\plus.png");
        addButton = new JButton("Add an GUEST",plusIcon);
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.setBackground(new Color(60, 165, 92)); // Set background color
        addButton.setForeground(new Color(235, 219, 204)); // Set foreground color
        addButton.setFocusPainted(false); // Remove focus border
        searchPanel.add(addButton); // Add "Add Invite" button to search panel

        mainPanel.add(searchPanel, BorderLayout.NORTH); // Add search panel to main panel

        // Add action listener to the "Add Invite" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddInviteForm(); // Afficher le formulaire d'ajout d'invité
            }
        });

        String[] columns = {"Invite ID", "Name", "Email", "Eventid"};

        // Récupération des données depuis la base de données
        Object[][] data = getinviteDataFromDatabase();
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
                    // Get the invite ID from the selected row
                    int Inviteid = (int) table.getValueAt(selectedRow, 0);
                    String name = (String) table.getValueAt(selectedRow, 1);
                    String Email = (String) table.getValueAt(selectedRow, 2);
                    int eventid = (int) table.getValueAt(selectedRow, 3);
                    // Open the modify invite form
                    createModifyInviteForm( Inviteid,name,Email, eventid);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to modify.");
                }
            }
        });

        // Create "Remove" button
        removeButton = new JButton("Remove Selected");
        removeButton.setPreferredSize(new Dimension(200, 40));
        removeButton.setBackground(new Color(60, 165, 92)); // Green background
        removeButton.setForeground(new Color(235, 219, 204)); // White text
        removeButton.setFocusPainted(false); // Remove focus border

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the invite ID from the selected row
                    int inviteId = (int) table.getValueAt(selectedRow, 0);
                    // Remove the row from the table model
                    tableModel.removeRow(selectedRow);
                    // Delete the invite from the database
                    deleteInviteFromDatabase(inviteId);
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
            	Window window = SwingUtilities.getWindowAncestor(Invite.this);
                window.dispose();
               Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });
    
        // Create a panel for the remove and modify buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE); // Set background color
        buttonsPanel.add(modifyButton); // Add modify button to the panel
        buttonsPanel.add(removeButton); // Add remove button to the panel
        buttonsPanel.add(backButton);
        // Add the buttons panel to the main panel's SOUTH position
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private void openModifyInviteForm(int Inviteid,String Name,String Email,int Eventid) {
        JDialog modifyDialog = new JDialog();
        modifyDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        modifyDialog.getContentPane().setBackground(Color.white); // Green background
        modifyDialog.getContentPane().setForeground(Color.black); // White text
        modifyDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel = createLabel("Modify Guest", new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        modifyDialog.getContentPane().add(titleLabel, gbc);

        // Create text fields with the same style as the addition form
        JTextField nameTextField = new JTextField();
        JTextField emailTextField = new JTextField();
        JTextField eventidTextField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(nameTextField, "Enter Name...");
        setTextFieldStyle(emailTextField, "Enter Email...");
        setTextFieldStyle(eventidTextField, "Enter Event ID...");

        // Set the text of the text fields to the provided values
        nameTextField.setText(Name);
        emailTextField.setText(Email);
        eventidTextField.setText(String.valueOf(Eventid));

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Name:", nameTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Email:", emailTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Event ID:", eventidTextField);

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
        modifyDialog.getContentPane().add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get modified data from the fields
                String newName = nameTextField.getText();
                String newEmail = emailTextField.getText();
                int newEventId = Integer.parseInt(eventidTextField.getText());
                // Update event data in the table
                int selectedRow = table.getSelectedRow();
                table.setValueAt(newName, selectedRow, 1);
                table.setValueAt(newEmail, selectedRow, 2);
                table.setValueAt(newEventId, selectedRow, 3);
                // Update event data in the database
                updateInviteInDatabase(Inviteid, newName, newEmail, newEventId);

                // Close the modification dialog
                modifyDialog.dispose();
            }
        });
        // Set dialog properties
        modifyDialog.setSize(400, 400);
        modifyDialog.setLocationRelativeTo(this);
        modifyDialog.setVisible(true);
    }
 
    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
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
  
    private Object[][] getinviteDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";
        
        // Liste pour stocker les données des tickets
        List<Object[]> ticketDataList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Créer la requête SQL pour récupérer les données des tickets
        	String query = "SELECT  * FROM invite";
        	
            // Préparer la déclaration SQL
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // Parcourir les résultats de la requête
                while (resultSet.next()) {
                 
                	int eventid = resultSet.getInt("Eventid");
                    int Inviteid = resultSet.getInt("Inviteid");
                    String name = resultSet.getString("Name");
                    String Email = resultSet.getString("Email");
                    // Créer un tableau d'objets contenant les valeurs des colonnes

                    Object[] rowData = { Inviteid, name, Email,eventid};
                    
                    // Ajouter le tableau d'objets à la liste
                    ticketDataList.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou d'exécution de requête
        }
        
        // Convertir la liste en un tableau à deux dimensions pour retourner les données des tickets
        return ticketDataList.toArray(new Object[0][]);
    }
 // Method to update an invite in the database
    private void updateInviteInDatabase(int Inviteid, String newName, String newEmail, int newEventid) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL UPDATE query
            String query = "UPDATE invite SET Name=?, Email=?, Eventid=? WHERE Inviteid=?";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setInt(3, newEventid);
            statement.setInt(4, Inviteid);

            // Execute the UPDATE query
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Invite updated successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL exceptions
        }
    }
    
    private void deleteInviteFromDatabase(int Inviteid) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL DELETE query
            String query = "DELETE FROM participant WHERE Inviteid = ?";
            
            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setLong(1, Inviteid);

            // Execute the deletion query
            int rowsDeleted = statement.executeUpdate();
            
            // Check if deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("Guest deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle connection or query execution errors
        }
    }
    private void addFieldWithLabel(GridBagConstraints gbc, JDialog dialog, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.getContentPane().add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.getContentPane().add(textField, gbc);
    }

    // Method to filter invites based on search text
    private void filterInvites() {
    	// Récupérer le texte saisi par l'utilisateur
        String Name = searchField.getText().trim().toLowerCase();

        // Récupérer les données depuis la base de données en fonction du Name filtré
        Object[][] filteredData = getinviteDataFromDatabase(Name);

        // Utiliser directement la variable de table au niveau de la classe
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();

        model.setRowCount(0); // Effacer les lignes existantes
        for (Object[] row : filteredData) {
            model.addRow(row); // Ajouter les lignes filtrées
        
        }
    }
    private Object[][] getinviteDataFromDatabase(String name) {
        // Connexion à votre base de données et exécution de la requête SQL pour récupérer les données
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM invite";
            
            // Ajout de la clause WHERE si un statut est spécifié
            if (name != null && !name.isEmpty()) {
                query += " WHERE Name LIKE ?";
            }
            
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Si un statut est spécifié, définissez le paramètre dans la requête
            if (name != null && !name.isEmpty()) {
                statement.setString(1, name + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            // Traitement des résultats et création des données filtrées
            List<Object[]> ticketData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[4];
                row[0] = resultSet.getString("Inviteid");
                row[1] = resultSet.getString("Name");
                row[2] = resultSet.getString("Email");
                row[3] = resultSet.getString("Eventid"); // Utilisez l'indice 4 pour le statut
                ticketData.add(row);
            }

            // Conversion de la liste en tableau à deux dimensions
            Object[][] data = new Object[ticketData.size()][];
            for (int i = 0; i < ticketData.size(); i++) {
                data[i] = ticketData.get(i);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    private void createModifyInviteForm(int Inviteid, String name, String email, int eventid) {
        // Create a modal dialog for the modify invite form
        JDialog modifyInviteDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Modify Invite", true);
        modifyInviteDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        modifyInviteDialog.getContentPane().setBackground(Color.white); // White background
        modifyInviteDialog.getContentPane().setForeground(Color.black); // Black text
        modifyInviteDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel = createLabel("Modify Invite", new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        modifyInviteDialog.getContentPane().add(titleLabel, gbc);

        // Create text fields for entering modified invite data
        JTextField nameTextField = new JTextField();
        JTextField emailTextField = new JTextField();
        JTextField eventidTextField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(nameTextField, "Enter Name...");
        setTextFieldStyle(emailTextField, "Enter Email...");
        setTextFieldStyle(eventidTextField, "Enter Event ID...");

        // Set the text of the text fields to the provided values
        nameTextField.setText(name);
        emailTextField.setText(email);
        eventidTextField.setText(String.valueOf(eventid));

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, modifyInviteDialog, "Name:", nameTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyInviteDialog, "Email:", emailTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyInviteDialog, "Event ID:", eventidTextField);

        // Create the "Save" button
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 165, 92)); // Green background
        saveButton.setForeground(new Color(235, 219, 204)); // White text
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        modifyInviteDialog.getContentPane().add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the modified data entered in the text fields
                String newName = nameTextField.getText(); // Modified name
                String newEmail = emailTextField.getText(); // Modified email
                int newEventid = Integer.parseInt(eventidTextField.getText()); // Modified eventid

                // Perform the update operation in the database
                updateInviteInDatabase(Inviteid, newName, newEmail, newEventid);

                // Refresh the table data
                filterInvites();

                // Close the dialog after updating the invite
                modifyInviteDialog.dispose();
            }
        });

        // Set dialog properties
        modifyInviteDialog.setSize(400, 300);
        modifyInviteDialog.setLocationRelativeTo(this);
        modifyInviteDialog.setVisible(true);
    }

    // Method to create and display the add invite form
    private void createAddInviteForm() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog addInviteDialog = new JDialog(parentFrame, "Add Invite", true);
        addInviteDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addInviteDialog.getContentPane().setBackground(Color.white); // White background
        addInviteDialog.getContentPane().setForeground(Color.black); // Black text
        addInviteDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel = createLabel("Add Invite", new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        addInviteDialog.getContentPane().add(titleLabel, gbc);

        // Create text fields with the same style as the addition form
        JTextField nameTextField = new JTextField();
        JTextField emailTextField = new JTextField();
        JTextField eventidTextField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(nameTextField, "Enter Name...");
        setTextFieldStyle(emailTextField, "Enter Email...");
        setTextFieldStyle(eventidTextField, "Enter Event ID...");

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, addInviteDialog, "Name:", nameTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addInviteDialog, "Email:", emailTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addInviteDialog, "Event ID:", eventidTextField);

        // Create the "Save" button
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 165, 92)); // Green background
        saveButton.setForeground(new Color(235, 219, 204)); // White text
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addInviteDialog.getContentPane().add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get data from text fields
                String name = nameTextField.getText();
                String email = emailTextField.getText();
                int eventid = Integer.parseInt(eventidTextField.getText());
                int Inviteid = Integer.parseInt(eventidTextField.getText());
                // Add invite to database
                addInviteToDatabase( Inviteid, name, email, eventid);

                // Close dialog
                addInviteDialog.dispose();
            }
        });

        // Set dialog properties
        addInviteDialog.setSize(400, 400);
        addInviteDialog.setLocationRelativeTo(this);
        addInviteDialog.setVisible(true);
    }
    // Method to add a ticket to the database
    private void addInviteToDatabase(int Inviteid,  String Name, String Email,int Eventid) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL INSERT query
            String query = "INSERT INTO billet (Inviteid , Name, Email, Eventid) VALUES (?, ?, ?, ?, ?)";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, Inviteid);
            statement.setString(2, Name);
            statement.setString(3, Email);
            statement.setLong(4, Eventid);

            // Execute the INSERT query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new Guest was added successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL exceptions
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Guests Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Invite inviteContentPanel = new Invite();
                frame.getContentPane().add(inviteContentPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
     }
}

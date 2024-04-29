package Projet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Invite extends JPanel {
    private JTextField searchField;
    private JButton addButton, modifyButton, removeButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public Invite() {
        setLayout(new BorderLayout());
        
        // Initialize mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Add title "Invités" to the main panel
        JLabel titleLabelEvents = new JLabel("Invités", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents, BorderLayout.NORTH);

        // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // Left-aligned flow layout
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        searchPanel.setBackground(new Color(235, 235, 235)); // Set background color

        // Create the search field with placeholder text
        searchField = new JTextField("Search for an Invite");
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
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\lenovo\\\\Downloads\\\\plus.png");
        addButton = new JButton("Add an Invite");
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

        String[] columns = {"Invite ID", "Nom", "Email"};

        // Récupération des données depuis la base de données
        Object[][] data = getInviteDataFromDatabase();
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
        modifyButton = new JButton("Modify Selected");
        modifyButton.setPreferredSize(new Dimension(200, 40));
        modifyButton.setBackground(new Color(60, 165, 92)); // Green background
        modifyButton.setForeground(new Color(235, 219, 204)); // White text
        modifyButton.setFocusPainted(false); // Remove focus border

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

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the invite ID from the selected row
                    int inviteId = (int) table.getValueAt(selectedRow, 0);
                    // Open the modify invite form
                    openModifyInviteForm(inviteId);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to modify.");
                }
            }
        });

        // Create a panel for the remove and modify buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE); // Set background color
        buttonsPanel.add(modifyButton); // Add modify button to the panel
        buttonsPanel.add(removeButton); // Add remove button to the panel

        // Add the buttons panel to the main panel's SOUTH position
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to delete an invite from the database
    private void deleteInviteFromDatabase(int inviteId) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL DELETE query
            String query = "DELETE FROM invite WHERE Inviteid = ?";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, inviteId); // Parameter for the ID of the invite to delete

            // Execute the deletion query
            int rowsDeleted = statement.executeUpdate();

            // Check if the deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("Invite deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle query execution or connection errors
        }
    }

    // Method to open the modify invite form
    private void openModifyInviteForm(int inviteId) {
        // Create a modal dialog for the modify invite form
        JDialog modifyInviteDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Modify Invite", true);
        modifyInviteDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create text fields for entering modified invite data
        JTextField nameField = new JTextField(); // Text field for modifying invite name
        JTextField emailField = new JTextField(); // Text field for modifying invite email

        gbc.gridx = 0;
        gbc.gridy = 0;
        addFieldWithLabel(gbc, modifyInviteDialog, "Name:", nameField); // Add label and text field for name
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyInviteDialog, "Email:", emailField); // Add label and text field for email
        gbc.gridy++;

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
        modifyInviteDialog.add(saveButton, gbc);


        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the modified data entered in the text fields
                String newName = nameField.getText(); // Modified name
                String newEmail = emailField.getText(); // Modified email

                // Perform the update operation in the database (not implemented here)

                // Close the dialog after updating the invite
                modifyInviteDialog.dispose();
            }
        });

        // Set dialog properties
        modifyInviteDialog.setSize(400, 200);
        modifyInviteDialog.setLocationRelativeTo(this);
        modifyInviteDialog.setVisible(true);
    }

    private void addFieldWithLabel(GridBagConstraints gbc, JDialog dialog, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(textField, gbc);
    }

    // Method to filter invites based on search text
    private void filterInvites() {
    	// Récupérer le texte saisi par l'utilisateur
        String Name = searchField.getText().trim().toLowerCase();

        // Récupérer les données depuis la base de données en fonction du Name filtré
        Object[][] filteredData = getInviteDataFromDatabase();

        // Utiliser directement la variable de table au niveau de la classe
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();

        model.setRowCount(0); // Effacer les lignes existantes
        for (Object[] row : filteredData) {
            model.addRow(row); // Ajouter les lignes filtrées
        
        }
    }

    // Method to retrieve invite data from the database
    private Object[][] getInviteDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        // List to store invite data
        List<Object[]> inviteDataList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create SQL query to retrieve invite data
            String query = "SELECT Inviteid, Nom, Email FROM invite";

            // Prepare SQL statement
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // Iterate through query results
                while (resultSet.next()) {
                    // Retrieve column values for each row
                    int inviteId = resultSet.getInt("Inviteid");
                    String nom = resultSet.getString("Nom");
                    String email = resultSet.getString("Email");

                    // Create an object array containing column values
                    Object[] rowData = {inviteId, nom, email};

                    // Add the object array to the list
                    inviteDataList.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection or query execution errors
        }

        // Convert the list to a two-dimensional array to return invite data
        return inviteDataList.toArray(new Object[0][]);
    }

    // Method to add an invite to the database
    private void addInviteToDatabase(String nom, String email) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL INSERT query
            String query = "INSERT INTO invite (Nom, Email) VALUES (?, ?)";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, email);

            // Execute the INSERT query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new invite was added successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL exceptions
        }
    }

    // Method to create and display the add invite form
    private void createAddInviteForm() {
        // Create a modal dialog for the add invite form
        JDialog addInviteDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Invite", true);
        addInviteDialog.setLayout(new GridLayout(0, 2, 10, 10));

        // Create labels and text fields for entering invite data
        JLabel nameLabel = new JLabel("Nom:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        // Add labels and text fields to the dialog
        addInviteDialog.add(nameLabel);
        addInviteDialog.add(nameField);
        addInviteDialog.add(emailLabel);
        addInviteDialog.add(emailField);

        // Create the "Save" button to add the invite to the table and database
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 165, 92)); // Green background
        saveButton.setForeground(new Color(235, 219, 204)); // White text
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the data entered in the text fields
                String nom = nameField.getText();
                String email = emailField.getText();

                // Add the invite to the table and database
                addInviteToDatabase(nom, email);

                // Close the dialog after adding the invite
                addInviteDialog.dispose();
            }
        });

        // Add the "Save" button to the dialog
        addInviteDialog.add(saveButton);

        // Set dialog properties
        addInviteDialog.setSize(300, 150);
        addInviteDialog.setLocationRelativeTo(this);
        addInviteDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Invités Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Invite inviteContentPanel = new Invite();
                frame.add(inviteContentPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
     }
}


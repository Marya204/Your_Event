package Projet;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Invite extends JPanel {
    private JPanel mainPanel;
    private JTextField searchField;
    private JDialog addInviteDialog;
    private JTable table;
    private DefaultTableModel tableModel;

    public Invite() {
        setLayout(new BorderLayout());

        // Initialize main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Utiliser un BoxLayout vertical
        // Add a marge in the top of the page
        mainPanel.add(Box.createVerticalStrut(20));
        // Add title "Participants" to the main panel
        JLabel titleLabelEvents = new JLabel("INVITES", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents, BorderLayout.NORTH);

        // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // Left-aligned flow layout
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        searchPanel.setBackground(new Color(235, 235, 235)); // Set background color

        // Create the search field with placeholder text
        searchField = new PlaceholderTextField("Search for an invite");
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(60, 165, 92))); // Set border color
        searchField.setBackground(Color.WHITE); // Set background color
        searchField.setForeground(Color.BLACK); // Set foreground color
        searchPanel.add(searchField);
        
        // Ajouter un écouteur sur le champ de recherche
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
        // Add space between components
        searchPanel.add(Box.createHorizontalStrut(300)); // Add space between search field and "Add Event" button

        // Create "Add Event" button with icon
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\hp\\\\Downloads\\\\Image\\\\Image\\\\plus.png"); // Change to your icon file path
        JButton addButton = new JButton("Add invite", plusIcon);
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
                createAddInviteForm(); // Afficher le formulaire d'ajout d'événement
            }
        });
    
        // Add table to the main panel
        String[] columns = { "Invite ID", "Name", "Email", "Event ID" };
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
               
                    int inviteID = (int) table.getValueAt(selectedRow, 0);

                    String name = (String) table.getValueAt(selectedRow, 1);
                    String email = (String) table.getValueAt(selectedRow, 2);
                   
                    int eventID = (int) table.getValueAt(selectedRow, 0);
                    // Open modification dialog with pre-filled data
                    openModifyInviteDialog(inviteID,name,email,eventID);
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
                    int inviteID = (int) table.getValueAt(selectedRow, 0);
                    // Remove the row from the table model
                    tableModel.removeRow(selectedRow);
                    // Delete the event from the database
                    deleteInviteFromDatabase(inviteID);
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
    private void openModifyInviteDialog(int inviteID, String name, String email, int eventID) {
        JDialog modifyDialog = new JDialog();
        modifyDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        modifyDialog.getContentPane().setBackground(Color.white); // Green background
        modifyDialog.getContentPane().setForeground(Color.black); // White text
        modifyDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel = createLabel("Modify Invite", new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        modifyDialog.add(titleLabel, gbc);

        // Create text fields with the same style as the addition form
        JTextField nameTextField = new JTextField();
        JTextField emailTextField = new JTextField();
        JTextField eventIDTextField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(nameTextField, "Enter Name...");
        setTextFieldStyle(emailTextField, "Enter Email...");
        setTextFieldStyle(eventIDTextField, "Enter Event ID...");

        // Set the text of the text fields to the provided values
        nameTextField.setText(name);
        emailTextField.setText(email);
        eventIDTextField.setText(String.valueOf(eventID));

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Name:", nameTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Email:", emailTextField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Event ID:", eventIDTextField);

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
                int newEventID = Integer.parseInt(eventIDTextField.getText());
                // Update event data in the table
                int selectedRow = table.getSelectedRow();
                table.setValueAt(newName, selectedRow, 1);
                table.setValueAt(newEmail, selectedRow, 2);
                table.setValueAt(newEventID, selectedRow, 3);
                // Update event data in the database
                updateInviteInDatabase(inviteID, newName, newEmail, newEventID);

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
    private void updateInviteInDatabase(int inviteID, String name, String email, int eventID) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "UPDATE invite SET Name = ?, Email = ?, EventID = ? WHERE InviteID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, eventID); // Utilisation de setInt pour Eventid
            statement.setInt(4, inviteID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Invite mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour de l'invité !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteInviteFromDatabase(int inviteID) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL DELETE query
            String query = "DELETE FROM invite WHERE InviteID = ?";
            
            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setLong(1, inviteID);

            // Execute the deletion query
            int rowsDeleted = statement.executeUpdate();
            
            // Check if deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("invite deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle connection or query execution errors
        }
    }
    
   
    
    private Object[][] getInviteDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM invite";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Object[]> inviteData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[4];
                row[0] = resultSet.getInt("InviteID"); // Correct column name
                row[1] = resultSet.getString("Name"); // Correct column name
                row[2] = resultSet.getString("Email");
                row[3] = resultSet.getInt("EventID");
                inviteData.add(row);
            }

            Object[][] data = new Object[inviteData.size()][];
            for (int i = 0; i < inviteData.size(); i++) {
                data[i] = inviteData.get(i);
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

    private void createAddInviteForm() {
        addInviteDialog = new JDialog();
        addInviteDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
      
        // Apply the same background color and font as the main panel
        addInviteDialog.getContentPane().setBackground(Color.white); // Green background
        addInviteDialog.getContentPane().setForeground(Color.black); // White text
        addInviteDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Add the title label
        JLabel titleLabel = createLabel("Add Event",new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        addInviteDialog.add(titleLabel, gbc);

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
        addFieldWithLabel(gbc, addInviteDialog, "Name:", NameField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addInviteDialog, "Email:", EmailField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addInviteDialog, "Event ID:", EventIDField);
      

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
        addInviteDialog.add(addButton, gbc);

        // Add action listener to the "Add" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the data entered in the form
                String Name = NameField.getText();
                String Email = EmailField.getText();
             
                int EventID = Integer.parseInt(EventIDField.getText());

                // Add the event to the database
                addInviteToDatabase(Name, Email,EventID);

                // Close the form after addition
                addInviteDialog.dispose();
            }
        });

        // Set the properties of the JDialog
        addInviteDialog.setSize(400, 400);
        addInviteDialog.setLocationRelativeTo(this);
        addInviteDialog.setVisible(true);
    }
    // Méthode pour ajouter un événement à la base de données
    private void addInviteToDatabase(String Name, String Email,int EventID) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO invite (Name, Email, EventID) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Name);
            statement.setString(2, Email);
            statement.setInt(3, EventID);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Invite ajouté avec succès !");
                // Refresh the table to display the new event
                refreshTable();
            } else {
                System.out.println("Échec de l'ajout de l'invité !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    // Méthode pour filtrer les invités dans la table en fonction de la recherche
    private void filterInvites() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        String text = searchField.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
    // Méthode pour rafraîchir la table avec les données de la base de données
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear the table
        Object[][] newData = getInviteDataFromDatabase(); // Get new data from the database
        for (Object[] row : newData) {
            tableModel.addRow(row); // Add each row to the table model
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
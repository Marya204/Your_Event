package Projet;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import Projet.Event;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Tickets extends JPanel {
    private JTextField searchField;
    private JButton addButton, modifyButton, removeButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public Tickets() {
        setLayout(new BorderLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Utiliser un BoxLayout vertical
        // Add a marge in the top of the page
        mainPanel.add(Box.createVerticalStrut(20));
        // Add title "Tickets" to the main panel
        JLabel titleLabelEvents = new JLabel("TICKETS", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92));
        mainPanel.add(titleLabelEvents, BorderLayout.NORTH);

        // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(235, 235, 235));

        // Create the search field with placeholder text
        searchField = new PlaceholderTextField("Search for a Ticket");
        searchField.setText("Search for a ticket");
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(60, 165, 92)));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.BLACK);
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterTickets();
            }

            public void removeUpdate(DocumentEvent e) {
                filterTickets();
            }

            public void changedUpdate(DocumentEvent e) {
                filterTickets();
            }
        });
        searchPanel.add(Box.createHorizontalStrut(300)); // Add space between search field and "Add Event" button

        // Create "Add Event" button with icon
        ImageIcon plusIcon = new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\Image\\plus.png"); // Change to your icon file path
        addButton = new JButton("Add a Ticket",plusIcon);
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.setBackground(new Color(60, 165, 92)); // Set background color
        addButton.setForeground(new Color(235, 219, 204)); // Set foreground color
        addButton.setFocusPainted(false); // Remove focus border
        searchPanel.add(addButton); // Add "Add Ticket" button to search panel

        mainPanel.add(searchPanel, BorderLayout.NORTH); // Add search panel to main panel

        // Add action listener to the "Add Event" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddTicketForm(); // Afficher le formulaire d'ajout de ticket
            }
        });

        String[] columns = {"ID", "EventID", "InviteID", "Price", "Status"};

        // Récupération des données depuis la base de données
        Object[][] data = getTicketDataFromDatabase();
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

        // Create remove button
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
                    // Get the ticket ID from the selected row
                    String ticketId = (String) table.getValueAt(selectedRow, 0);
                    // Remove the row from the table model
                    tableModel.removeRow(selectedRow);
                    // Delete the ticket from the database
                    deleteTicketFromDatabase(ticketId);
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
                    String ticketId = (String) table.getValueAt(selectedRow, 0);
                    String eventId = (String) table.getValueAt(selectedRow, 1);
                    String inviteId = (String) table.getValueAt(selectedRow, 2);
                    String price = (String) table.getValueAt(selectedRow, 3);
                    String status = (String) table.getValueAt(selectedRow, 4);

                    // Create a dialog to modify ticket details
                    createModifyTicketForm(ticketId, eventId, inviteId, price, status);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to modify.");
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
            	Window window = SwingUtilities.getWindowAncestor(Tickets.this);
                window.dispose();
               Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });

        // Create a panel for the remove button
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE); // Set background color
        buttonsPanel.add(modifyButton); // Add modify button to the panel
        buttonsPanel.add(removeButton); // Add remove button to the panel
        buttonsPanel.add(backButton); // Add back button to the panel


        // Add the buttons panel to the main panel's SOUTH position
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(mainPanel);

        setVisible(true);
    }

 // Method to delete a ticket from the database
    private void deleteTicketFromDatabase(String ticketId) {
        String url = "jdbc:mysql://localhost:3306/events"; // URL de connexion à votre base de données MySQL
        String username = "root"; // Nom d'utilisateur de la base de données
        String password = ""; // Mot de passe de la base de données

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Créer la requête SQL DELETE
            String query = "DELETE FROM billet WHERE Id = ?";

            // Préparer la déclaration SQL
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, ticketId); // Paramètre pour l'ID du ticket à supprimer

            // Exécuter la requête de suppression
            int rowsDeleted = statement.executeUpdate();

            // Vérifier si la suppression a été réussie
            if (rowsDeleted > 0) {
                System.out.println("Ticket deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les erreurs d'exécution de la requête ou de connexion
        }
    }

 // Method to retrieve ticket data from the database
    private Object[][] getTicketDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";
        
       
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Créer la requête SQL pour récupérer les données des tickets
        	String query = "SELECT * FROM billet ";
        	  Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery(query);
        	

           
            // Liste pour stocker les données des tickets
            List<Object[]> ticketDataList = new ArrayList<>();

                // Parcourir les résultats de la requête
                while (resultSet.next()) {
                	 Object[] row = new Object[5];
                     row[0] = resultSet.getString("Id");
                     row[0] = resultSet.getString("Eventid");
                     row[1] = resultSet.getString("Inviteid");
                     row[2] = resultSet.getString("Price");
                     row[3] = resultSet.getString("Status");
                     
                     ticketDataList.add(row);
                 }
                
           
        // Conversion de la liste en tableau à deux dimensions
        Object[][] data = new Object[ticketDataList.size()][];
        for (int i = 0; i < ticketDataList.size(); i++) {
            data[i] = ticketDataList.get(i);   
    }
        return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }
    

    private void filterTickets() {
        // Récupérer le texte saisi par l'utilisateur
        String Status = searchField.getText().trim().toLowerCase();

        // Récupérer les données depuis la base de données en fonction du lieu filtré
        Object[][] filteredData = getTicketDataFromDatabase(Status);

        // Utiliser directement la variable de table au niveau de la classe
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();

        model.setRowCount(0); // Effacer les lignes existantes
        for (Object[] row : filteredData) {
            model.addRow(row); // Ajouter les lignes filtrées
        }
    }
    private Object[][] getTicketDataFromDatabase(String status) {
        // Connexion à votre base de données et exécution de la requête SQL pour récupérer les données
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM billet";
            
            // Ajout de la clause WHERE si un statut est spécifié
            if (status != null && !status.isEmpty()) {
                query += " WHERE Status LIKE ?";
            }
            
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Si un statut est spécifié, définissez le paramètre dans la requête
            if (status != null && !status.isEmpty()) {
                statement.setString(1, status + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            // Traitement des résultats et création des données filtrées
            List<Object[]> ticketData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("Id");
                row[1] = resultSet.getString("Eventid");
                row[2] = resultSet.getString("Inviteid");
                row[3] = resultSet.getString("Price");
                row[4] = resultSet.getString("Status"); // Utilisez l'indice 4 pour le statut
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



 // Method to add a ticket to the database
    private void addTicketToDatabase(String ticketId, String eventId, String inviteId, String status, String price) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL INSERT query
            String query = "INSERT INTO billet (ID, EventID, InviteID, Price, Status) VALUES (?, ?, ?, ?, ?)";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticketId);
            statement.setString(2, eventId);
            statement.setString(3, inviteId);
            statement.setString(4, price);
            statement.setString(5, status);

            // Execute the INSERT query
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new ticket was added successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL exceptions
        }
    }

    // Method to add a labeled field to a dialog with GridBagLayout
    private void addFieldWithLabel(GridBagConstraints gbc, JDialog dialog, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.getContentPane().add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.getContentPane().add(textField, gbc);
    }


 // Method to create and display the add ticket form
    private void createAddTicketForm() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog addTicketDialog = new JDialog(parentFrame, "Add Ticket", true);
        addTicketDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addTicketDialog.getContentPane().setBackground(Color.white); // White background
        addTicketDialog.getContentPane().setForeground(Color.black); // Black text
        addTicketDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel = createLabel("Add Ticket", new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        addTicketDialog.getContentPane().add(titleLabel, gbc);

        // Create text fields with the same style as the addition form
        JTextField ticketIdField = new JTextField();
        JTextField eventIdField = new JTextField();
        JTextField inviteIdField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField priceField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(ticketIdField, "Enter Ticket ID...");
        setTextFieldStyle(eventIdField, "Enter Event ID...");
        setTextFieldStyle(inviteIdField, "Enter Invite ID...");
        setTextFieldStyle(statusField, "Enter Status...");
        setTextFieldStyle(priceField, "Enter Price...");

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Ticket ID:", ticketIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Event ID:", eventIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Invite ID:", inviteIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Status:", statusField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Price:", priceField);
        gbc.gridy++;

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
        addTicketDialog.getContentPane().add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticketId = ticketIdField.getText();
                String eventId = eventIdField.getText();
                String inviteId = inviteIdField.getText();
                String status = statusField.getText();
                String price = priceField.getText();

                addTicketToDatabase(ticketId, eventId, inviteId, status, price);
                addTicketDialog.dispose();
            }
        });

        // Set dialog properties
        addTicketDialog.setSize(400, 400);
        addTicketDialog.setLocationRelativeTo(this);
        addTicketDialog.setVisible(true);
    }

    private void createModifyTicketForm(String ticketId, String eventId, String inviteId, String price, String status) {
        JDialog modifyDialog = new JDialog();
        modifyDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        modifyDialog.getContentPane().setBackground(Color.white); // White background
        modifyDialog.getContentPane().setForeground(Color.black); // Black text
        modifyDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Create and customize the title label
        JLabel titleLabel1 = createLabel("Modify Ticket", new Color(60, 165, 92));
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        modifyDialog.getContentPane().add(titleLabel1, gbc);

        // Create text fields with the same style as the addition form
        JTextField eventIdField = new JTextField();
        JTextField inviteIdField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField statusField = new JTextField();

        // Apply the same text field style as the addition form
        setTextFieldStyle(eventIdField, "Enter Event ID...");
        setTextFieldStyle(inviteIdField, "Enter Invite ID...");
        setTextFieldStyle(priceField, "Enter Price...");
        setTextFieldStyle(statusField, "Enter Status...");

        // Set the text of the text fields to the provided values
        eventIdField.setText(eventId);
        inviteIdField.setText(inviteId);
        priceField.setText(price);
        statusField.setText(status);

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Event ID:", eventIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Invite ID:", inviteIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Price:", priceField);
        gbc.gridy++;
        addFieldWithLabel(gbc, modifyDialog, "Status:", statusField);

        // Create the "Save" button
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(60, 165, 92)); // Green background
        saveButton.setForeground(new Color(235, 219, 204)); // White text
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        modifyDialog.getContentPane().add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get modified data from the fields
                String newEventId = eventIdField.getText();
                String newInviteId = inviteIdField.getText();
                String newPrice = priceField.getText();
                String newStatus = statusField.getText();

                // Get the selected row index
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    // Update the table model with the modified data
                    tableModel.setValueAt(newEventId, selectedRow, 1);
                    tableModel.setValueAt(newInviteId, selectedRow, 2);
                    tableModel.setValueAt(newPrice, selectedRow, 3);
                    tableModel.setValueAt(newStatus, selectedRow, 4);

                    // Update the ticket in the database
                    updateTicketInDatabase(ticketId, newEventId, newInviteId, newPrice, newStatus);

                    // Close the dialog after saving changes
                    modifyDialog.dispose();
                }
            }
        });
        // Set dialog properties
        modifyDialog.setSize(400, 300);
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
 // Method to update a ticket in the database
    private void updateTicketInDatabase(String ticketId, String eventId, String inviteId, String status, String price) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL UPDATE query
            String query = "UPDATE billet SET EventID = ?, InviteID = ?, Price = ?, Status = ? WHERE ID = ?";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, eventId);
            statement.setString(2, inviteId);
            statement.setString(3, price);
            statement.setString(4, status);
            statement.setString(5, ticketId);

            // Execute the UPDATE query
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Ticket updated successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL exceptions
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Tickets Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Tickets TicketstContentPanel = new Tickets();
                frame.getContentPane().add(TicketstContentPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

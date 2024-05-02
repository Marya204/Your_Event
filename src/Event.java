package Projet;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class Event extends JFrame {
  
    private JDialog addEventDialog;
    private JPanel mainPanel;
    private JTextField searchField; // Declare searchField at the class level
    private JTable table; // Declare table at the class level
    private DefaultTableModel tableModel;
   


    public Event() {
        setTitle("Event Management");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
       
        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical layout
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Add title "Événements" to the main panel
        JLabel titleLabelEvents = new JLabel("EVENTS ", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents);

     // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // Left-aligned flow layout
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        searchPanel.setBackground(new Color(235, 235, 235)); // Set background color

     // Create the search field with placeholder text
        searchField = new PlaceholderTextField("Search for an event");

        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(60, 165, 92))); // Set border color
        searchField.setBackground(Color.WHITE); // Set background color
        searchField.setForeground(Color.BLACK); // Set foreground color

        
        // Add the search field to the search panel
        searchPanel.add(searchField);
     // Ajouter un écouteur sur le champ de recherche
        searchField.getDocument().addDocumentListener(new DocumentListener() {
        
            public void insertUpdate(DocumentEvent e) {
                filterEvents();
            }

            public void removeUpdate(DocumentEvent e) {
                filterEvents();
            }
            public void changedUpdate(DocumentEvent e) {
                filterEvents();
            }
        });

   
        // Add space between components
        searchPanel.add(Box.createHorizontalStrut(300)); // Add space between search field and "Add Event" button

        
     // Create "Add Event" button with icon
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\hp\\\\Downloads\\Image \\Image\\plus.png"); // Change "path_to_your_icon_file.png" to the actual path of your icon file
        JButton addButton = new JButton("Add an event", plusIcon);
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.setBackground(new Color(60, 165, 92)); // Set background color
        addButton.setForeground(new Color(235, 219, 204)); // Set foreground color
        addButton.setFocusPainted(false); // Remove focus border
        searchPanel.add(addButton);


        mainPanel.add(searchPanel, BorderLayout.NORTH); // Add search panel to main panel

     

     // Ajouter une method pour créer et afficher le formulaire d'ajout d'événement
       
        // Modifier l'actionListener du bouton "Add Event" pour afficher le formulaire
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddEventForm(); // Afficher le formulaire d'ajout d'événement
            }
        });

                
        String[] columns = { "Events ID", "Title", "Description", "Date", "Location", "Type", "Status", "Price", "Capacity" };


        // Récupération des données depuis la base de données
        Object[][] data = getEventDataFromDatabase();
        table = new JTable(data, columns);
        this.tableModel = new DefaultTableModel(data, columns);
        this.table = new JTable(this.tableModel);
     

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 165, 92));
        table.getTableHeader().setForeground(new Color(235, 219, 204));
        table.setSelectionBackground(new Color(181, 172, 73));
        table.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane);
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
                    String eventId = (String) table.getValueAt(selectedRow, 0);
                    String title = (String) table.getValueAt(selectedRow, 1);
                    String description = (String) table.getValueAt(selectedRow, 2);
                    String date = (String) table.getValueAt(selectedRow, 3);
                    String location = (String) table.getValueAt(selectedRow, 4);
                    String type = (String) table.getValueAt(selectedRow, 5);
                    String status = (String) table.getValueAt(selectedRow, 6);
                    String price = (String) table.getValueAt(selectedRow, 7);
                    String capacity = (String) table.getValueAt(selectedRow, 8);
                    
                    // Open modification dialog with pre-filled data
                    openModifyEventDialog(eventId, title, description, date, location, type, status, price, capacity);
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
                    String eventId = (String) table.getValueAt(selectedRow, 0);
                    // Remove the row from the table model
                    tableModel.removeRow(selectedRow);
                    // Delete the event from the database
                    deleteEventFromDatabase(eventId);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to remove.");
                }
            }
        });
        
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setBackground(new Color(60, 165, 92)); // Green background
        backButton.setForeground(new Color(235, 219, 204)); // White text
        backButton.setFocusPainted(false); // Remove focus border
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
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

        // Create a panel for the remove button
        JPanel backPanel = new JPanel();
        backPanel.setBackground(Color.WHITE); // Set background color
        backPanel.add(removeButton); // Add remove button to the panel
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
    
    private void openModifyEventDialog(String eventId, String title, String description, String date, String location, String type, String status, String price, String capacity) {
        JDialog modifyDialog = new JDialog(this, "Modify Event", true);
        modifyDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create title label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        modifyDialog.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        modifyDialog.add(new JTextField(title), gbc);

        // Create description label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        modifyDialog.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        modifyDialog.add(new JTextField(description), gbc);

        // Create date label and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        modifyDialog.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        modifyDialog.add(new JTextField(date), gbc);

        // Create location label and text field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        modifyDialog.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        modifyDialog.add(new JTextField(location), gbc);
        // Create type label and text field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        modifyDialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        modifyDialog.add(new JTextField(type), gbc);
        // Create status label and text field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 5;
        modifyDialog.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 5;
        modifyDialog.add(new JTextField(status), gbc);
        // Create price label and text field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 6;
        modifyDialog.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 6;
        modifyDialog.add(new JTextField(price), gbc);
        
        // Create capacity label and text field
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 7;
        modifyDialog.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 7;
        modifyDialog.add(new JTextField(capacity), gbc);
        // Repeat for other fields...

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
                String newTitle = ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(1)).getComponent(1)).getText();
                String newDescription = ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(2)).getComponent(1)).getText();
                String newDate = ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(3)).getComponent(1)).getText();
                String newLocation= ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(4)).getComponent(1)).getText();
                String newType = ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(5)).getComponent(1)).getText();
                String newStatus = ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(6)).getComponent(1)).getText();
                String newPrice = ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(7)).getComponent(1)).getText();
                String newCapacity= ((JTextField) ((Container) modifyDialog.getContentPane().getComponent(8)).getComponent(1)).getText();
                // Update event data in the table
                int selectedRow = table.getSelectedRow();
                table.setValueAt(newTitle, selectedRow, 1);
                table.setValueAt(newDescription, selectedRow, 2);
                // Repeat for other fields...

                // Update event data in the database
                updateEventInDatabase(eventId, newTitle, newDescription,newDate,newLocation,newType,newStatus,newPrice,newCapacity);

                // Close the modification dialog
                modifyDialog.dispose();
            }
        });

        // Set dialog properties
        modifyDialog.setSize(600, 500);
        modifyDialog.setLocationRelativeTo(this);
        modifyDialog.setVisible(true);
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

    // Method to update event data in the database
    private void updateEventInDatabase(String eventId, String title, String description, String date, String location, String type, String status, String price, String capacity) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "UPDATE event SET Titre = ?, Description = ?, Date = ?, Lieu = ?, Type = ?, Status = ?, Prix = ?, Capacite = ? WHERE Eventid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, date);
            statement.setString(4, location);
            statement.setString(5, type);
            statement.setString(6, status);
            statement.setString(7, price);
            statement.setString(8, capacity);
            statement.setString(9, eventId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Event updated successfully!");
            } else {
                System.out.println("Failed to update event!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create a JLabel with specified text and color
    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }
	private void deleteEventFromDatabase(String eventId) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create the SQL DELETE query
            String query = "DELETE FROM event WHERE Eventid = ?";
            
            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, eventId);

            // Execute the deletion query
            int rowsDeleted = statement.executeUpdate();
            
            // Check if deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("Event deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle connection or query execution errors
        }
    }
    

    private Object[][] getEventDataFromDatabase() {
        // Connexion à votre base de données et exécution de la requête SQL pour récupérer les données
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM event";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Création d'une liste pour stocker les données
            List<Object[]> eventData = new ArrayList<>();

            // Parcours des résultats de la requête et ajout des données à la liste
            while (resultSet.next()) {
                Object[] row = new Object[9]; // 9 colonnes dans la table evenements
                row[0] = resultSet.getString("Eventid");
                row[1] = resultSet.getString("Titre");
                row[2] = resultSet.getString("Description");
                row[3] = resultSet.getString("Date");
                row[4] = resultSet.getString("Lieu");
                row[5] = resultSet.getString("Type");
                row[6] = resultSet.getString("Status");
                row[7] = resultSet.getString("Prix");
                row[8] = resultSet.getString("Capacite");
                eventData.add(row);
            }
            // Conversion de la liste en tableau à deux dimensions
            Object[][] data = new Object[eventData.size()][];
            for (int i = 0; i < eventData.size(); i++) {
                data[i] = eventData.get(i);
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
  

    private void createAddEventForm() {
        addEventDialog = new JDialog(this, "ADD EVENT", true);
        addEventDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
      
        // Apply the same background color and font as the main panel
        addEventDialog.getContentPane().setBackground(Color.white); // Green background
        addEventDialog.getContentPane().setForeground(Color.black); // White text
        addEventDialog.setFont(new Font("Arial", Font.PLAIN, 18)); // Same font as main panel

        // Add the title label
        JLabel titleLabel = createLabel("Add Event",new Color(60, 165, 92));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        addEventDialog.add(titleLabel, gbc);

        // Create text fields with white background and black text
        JTextField titreField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField lieuField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField StatusField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField capaciteField = new JTextField();

        // Call setTextFieldStyle for each text field
        setTextFieldStyle(titreField, "Enter title...");
        setTextFieldStyle(descriptionField, "Enter description...");
        setTextFieldStyle(dateField, "Enter date...");
        setTextFieldStyle(lieuField, "Enter location...");
        setTextFieldStyle(typeField, "Enter type...");
        setTextFieldStyle(StatusField, "Enter status...");
        setTextFieldStyle(prixField, "Enter price...");
        setTextFieldStyle(capaciteField, "Enter capacity...");

        // Apply the same background and foreground colors as the main panel
        titreField.setBackground(Color.WHITE);
        titreField.setForeground(Color.BLACK);
        descriptionField.setBackground(Color.WHITE);
        descriptionField.setForeground(Color.BLACK);
        dateField.setBackground(Color.WHITE);
        dateField.setForeground(Color.BLACK);
        lieuField.setBackground(Color.WHITE);
        lieuField.setForeground(Color.BLACK);
        typeField.setBackground(Color.WHITE);
        typeField.setForeground(Color.BLACK);
        StatusField.setBackground(Color.WHITE);
        StatusField.setForeground(Color.BLACK);
        prixField.setBackground(Color.WHITE);
        prixField.setForeground(Color.BLACK);
        capaciteField.setBackground(Color.WHITE);
        capaciteField.setForeground(Color.BLACK);

        // Add fields with labels
        gbc.gridwidth = 1; // Reset grid width
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Title:", titreField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Description:", descriptionField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Date:", dateField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Location:", lieuField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Type:", typeField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Status:", StatusField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Price:", prixField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addEventDialog, "Capacity:", capaciteField);

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
                String titre = titreField.getText();
                String description = descriptionField.getText();
                String date = dateField.getText();
                String lieu = lieuField.getText();
                String type = typeField.getText();
                String Status = StatusField.getText();
                String prix = prixField.getText();
                String capacite = capaciteField.getText();

                // Add the event to the database
                addEventToDatabase(titre, description, date, lieu, type, Status, prix, capacite);

                // Close the form after addition
                addEventDialog.dispose();
            }
        });

        // Set the properties of the JDialog
        addEventDialog.setSize(600, 500);
        addEventDialog.setLocationRelativeTo(this);
        addEventDialog.setVisible(true);
    }

    private void filterEvents() {
        // Récupérer le texte saisi par l'utilisateur
        String lieu = searchField.getText().trim().toLowerCase();

        // Récupérer les données depuis la base de données en fonction du lieu filtré
        Object[][] filteredData = getEventDataFromDatabase(lieu);

        // Utiliser directement la variable de table au niveau de la classe
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();

        model.setRowCount(0); // Effacer les lignes existantes
        for (Object[] row : filteredData) {
            model.addRow(row); // Ajouter les lignes filtrées
        }
    }

    private Object[][] getEventDataFromDatabase(String lieu) {
        // Connexion à votre base de données et exécution de la requête SQL pour récupérer les données
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM event";
            
            // Ajout de la clause WHERE si un lieu est spécifié
            if (lieu != null && !lieu.isEmpty()) {
                query += " WHERE Lieu LIKE ?";
            }
            
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Si un lieu est spécifié, définissez le paramètre dans la requête
            if (lieu != null && !lieu.isEmpty()) {
                statement.setString(1, "%" + lieu + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            // Traitement des résultats et création des données filtrées
            List<Object[]> eventData = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[9];
                row[0] = resultSet.getString("Eventid");
                row[1] = resultSet.getString("Titre");
                row[2] = resultSet.getString("Description");
                row[3] = resultSet.getString("Date");
                row[4] = resultSet.getString("Lieu");
                row[5] = resultSet.getString("Type");
                row[6] = resultSet.getString("Status");
                row[7] = resultSet.getString("Prix");
                row[8] = resultSet.getString("Capacite");
                eventData.add(row);
            }

            // Conversion de la liste en tableau à deux dimensions
            Object[][] data = new Object[eventData.size()][];
            for (int i = 0; i < eventData.size(); i++) {
                data[i] = eventData.get(i);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }


 // Ajouter une méthode pour ajouter un événement à la base de données
    private void addEventToDatabase(String titre, String description, String date, String lieu, String type,String Status, String prix, String capacite) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Créer la requête SQL d'insertion
            String query = "INSERT INTO event (Titre, Description, Date, Lieu, Type, Status,Prix, Capacite) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            // Préparer la déclaration SQL
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Définir les valeurs des paramètres dans la requête SQL
            statement.setString(1, titre);
            statement.setString(2, description);
            statement.setString(3, date);
            statement.setString(4, lieu);
            statement.setString(5, type);
            statement.setString(6, Status);
            statement.setString(7, prix);
            statement.setString(8, capacite);

            // Exécuter la requête d'insertion
            int rowsInserted = statement.executeUpdate();
            
            // Vérifier si l'insertion a réussi
            if (rowsInserted > 0) {
                System.out.println("L'événement a été ajouté avec succès !");
                // Actualiser l'affichage si nécessaire
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Gérer les erreurs de connexion ou d'exécution de la requête
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
                new Event();
            }
        });
    }
}
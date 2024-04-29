package Projet;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
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
        add(mainPanel);

        // Add title "Tickets" to the main panel
        JLabel titleLabelEvents = new JLabel("Tickets", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92));
        mainPanel.add(titleLabelEvents, BorderLayout.NORTH);

        // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(235, 235, 235));

        // Create the search field with placeholder text
        searchField = new JTextField("Search for a Ticket");
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
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\lenovo\\\\Downloads\\\\plus.png");
        addButton = new JButton("Add a Ticket");
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

        // R�cup�ration des donn�es depuis la base de donn�es
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

        // Create a panel for the remove button
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE); // Set background color
        buttonsPanel.add(modifyButton); // Add modify button to the panel
        buttonsPanel.add(removeButton); // Add remove button to the panel

        // Add the buttons panel to the main panel's SOUTH position
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

 // Method to delete a ticket from the database
    private void deleteTicketFromDatabase(String ticketId) {
        String url = "jdbc:mysql://localhost:3306/events"; // URL de connexion � votre base de donn�es MySQL
        String username = "root"; // Nom d'utilisateur de la base de donn�es
        String password = ""; // Mot de passe de la base de donn�es

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Cr�er la requ�te SQL DELETE
            String query = "DELETE FROM billet WHERE Id = ?";

            // Pr�parer la d�claration SQL
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, ticketId); // Param�tre pour l'ID du ticket � supprimer

            // Ex�cuter la requ�te de suppression
            int rowsDeleted = statement.executeUpdate();

            // V�rifier si la suppression a �t� r�ussie
            if (rowsDeleted > 0) {
                System.out.println("Ticket deleted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // G�rer les erreurs d'ex�cution de la requ�te ou de connexion
        }
    }

    private void filterTickets() {
        // R�cup�rer le texte saisi par l'utilisateur
        String lieu = searchField.getText().trim().toLowerCase();

        // R�cup�rer les donn�es depuis la base de donn�es en fonction du lieu filtr�
        Object[][] filteredData = getTicketDataFromDatabase();

        // Utiliser directement la variable de table au niveau de la classe
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();

        model.setRowCount(0); // Effacer les lignes existantes
        for (Object[] row : filteredData) {
            model.addRow(row); // Ajouter les lignes filtr�es
        }
    }
 // Method to retrieve ticket data from the database
    private Object[][] getTicketDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";
        
        // Liste pour stocker les donn�es des tickets
        List<Object[]> ticketDataList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Cr�er la requ�te SQL pour r�cup�rer les donn�es des tickets
        	String query = "SELECT ID, EventID, InviteID, Prix, Status FROM billet";
        	
            // Pr�parer la d�claration SQL
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // Parcourir les r�sultats de la requ�te
                while (resultSet.next()) {
                    // R�cup�rer les valeurs des colonnes pour chaque ligne
                	String id = resultSet.getString("ID");
                    String eventId = resultSet.getString("EventID");
                    String inviteId = resultSet.getString("InviteID");
                    String price = resultSet.getString("Prix");
                    String status = resultSet.getString("Status");
                    // Cr�er un tableau d'objets contenant les valeurs des colonnes

                    Object[] rowData = {id, eventId, inviteId, price, status};
                    
                    // Ajouter le tableau d'objets � la liste
                    ticketDataList.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // G�rer les erreurs de connexion ou d'ex�cution de requ�te
        }
        
        // Convertir la liste en un tableau � deux dimensions pour retourner les donn�es des tickets
        return ticketDataList.toArray(new Object[0][]);
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
        dialog.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(textField, gbc);
    }


 // Method to create and display the add ticket form
    private void createAddTicketForm() {
        // Obtenir le JFrame parent
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Cr�er un JDialog avec le JFrame parent
        JDialog addTicketDialog = new JDialog(parentFrame, "Add Ticket", true);
        addTicketDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create text fields for entering ticket data
        JTextField ticketIdField = new JTextField(); // New text field for manual entry of ticket ID
        JTextField eventIdField = new JTextField();
        JTextField inviteIdField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField priceField = new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        addFieldWithLabel(gbc, addTicketDialog, "Ticket ID:", ticketIdField); // Add label and text field for ticket ID
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Event ID:", eventIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Invite ID:", inviteIdField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Status:", statusField);
        gbc.gridy++;
        addFieldWithLabel(gbc, addTicketDialog, "Price:", priceField);
        gbc.gridy++;

        // Create the "Save" button to add the ticket to the table and database
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(60, 165, 92)); // Green background
        saveButton.setForeground(new Color(235, 219, 204)); // White text
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addTicketDialog.add(saveButton, gbc);

        // Add action listener to the "Save" button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the data entered in the text fields
                String ticketId = ticketIdField.getText(); // Retrieve manually entered ticket ID
                String eventId = eventIdField.getText();
                String inviteId = inviteIdField.getText();
                String status = statusField.getText();
                String price = priceField.getText();

                // Add the ticket to the table and database
                addTicketToDatabase(ticketId, eventId, inviteId, status, price);

                // Close the dialog after adding the ticket
                addTicketDialog.dispose();
            }
        });

        // Set dialog properties
        addTicketDialog.setSize(400, 300);
        addTicketDialog.setLocationRelativeTo(this);
        addTicketDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Tickets Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Tickets TicketstContentPanel = new Tickets();
                frame.add(TicketstContentPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

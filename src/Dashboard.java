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
import javax.swing.JOptionPane;


public class Dashboard extends JFrame {
    private JPanel sidebarPanel;
    private JButton dashboardButton, settingsButton, helpButton,invitebutton, logoutButton;
    private JDialog addEventDialog;
    private JPanel mainPanel;
    private JTextField searchField; // Declare searchField at the class level
    private JTable table; // Declare table at the class level
    private DefaultTableModel tableModel;

    public Dashboard() {
        setTitle("YourEvent");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        // Création du panneau latéral (sidebar)
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(60, 165, 92); // #3CA55C
                Color color2 = new Color(181, 172, 73); // #B5AC49
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // Create title label with icon
        JLabel titleLabel = new JLabel(" YourEvent", new ImageIcon("C:\\Users\\lenovo\\Downloads\\logoEvent.png"),
                JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(235, 219, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons with transparent background
        dashboardButton = createButton("Evénements", new Color(0, 102, 51)); // Green color
        settingsButton = createButton("Billets", new Color(60, 165, 92)); // Light green color
        helpButton = createButton("Participants", new Color(0, 102, 51)); // Green color
        invitebutton=createButton("Invités", new Color(0, 102, 51)); // Green color
        dashboardButton.setIcon(new ImageIcon("C:\\Users\\lenovo\\Downloads\\evenement.png"));
        settingsButton.setIcon(new ImageIcon("C:\\Users\\lenovo\\Downloads\\billets-davion.png"));
        helpButton.setIcon(new ImageIcon("C:\\Users\\lenovo\\Downloads\\participation.png"));
        invitebutton.setIcon(new ImageIcon("C:\\Users\\lenovo\\Downloads\\invites.png"));
        // Create logout button with icon
        logoutButton = new JButton("Log out",
                new ImageIcon("C:\\Users\\lenovo\\Downloads\\Ionic-Ionicons-Log-out-outline.32.png"));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 17));
        logoutButton.setForeground(new Color(235, 219, 204));
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between title and buttons
        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add space between buttons
        sidebarPanel.add(settingsButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add space between buttons
        sidebarPanel.add(helpButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Add space between buttons
        sidebarPanel.add(invitebutton);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutButton); // Add logout button at the bottom

        // Set preferred size of the sidebarPanel
        sidebarPanel.setPreferredSize(new Dimension(200, sidebarPanel.getPreferredSize().height));

        getContentPane().add(sidebarPanel, BorderLayout.WEST);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical layout
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Add title "Événements" to the main panel
        JLabel titleLabelEvents = new JLabel("Événements", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents);

     // Add search panel to the main panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // Left-aligned flow layout
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        searchPanel.setBackground(new Color(235, 235, 235)); // Set background color

     // Create the search field with placeholder text
        searchField = new PlaceholderTextField("Rechercher un évenement");

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
        ImageIcon plusIcon = new ImageIcon("C:\\\\Users\\\\lenovo\\\\Downloads\\\\plus.png"); // Change "path_to_your_icon_file.png" to the actual path of your icon file
        JButton addButton = new JButton("Ajouter un évenement", plusIcon);
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

   

      
        
        
        String[] columns = { "Events ID", "Title", "Description", "Date", "Location", "Type", "Status", "Price", "Capacity", "Supprimer" };


        // Récupération des données depuis la base de données
        Object[][] data = getEventDataFromDatabase();
        table = new JTable(data, columns);
        this.tableModel = new DefaultTableModel(data, columns);
        this.table = new JTable(this.tableModel);
     // Ajouter la nouvelle colonne "Supprimer" avec des boutons "Supprimer"
        table.getColumn("Supprimer").setCellRenderer(new DeleteButtonRenderer());
        table.getColumn("Supprimer").setCellEditor(new DeleteButtonEditor(new JCheckBox()));

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(60, 165, 92));
        table.getTableHeader().setForeground(new Color(235, 219, 204));
        table.setSelectionBackground(new Color(181, 172, 73));
        table.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane);
        setVisible(true);
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
    private JLabel createLabel(String text, Color textColor) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor); // Set text color
        label.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font
        return label;
    }

    private void createAddEventForm() {
        addEventDialog = new JDialog(this, "Ajouter un évenement", true);
        addEventDialog.setLayout(new GridLayout(0, 2));

        // Apply the same background color and font as the main panel
        addEventDialog.getContentPane().setBackground(new Color(60, 165, 92)); // Green background
        addEventDialog.getContentPane().setForeground(new Color(235, 219, 204)); // White text
        addEventDialog.setFont(new Font("Arial", Font.BOLD, 18)); // Same font as main panel

        // Create text fields with white background and black text
        JTextField titreField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField lieuField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField capaciteField = new JTextField();

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
        prixField.setBackground(Color.WHITE);
        prixField.setForeground(Color.BLACK);
        capaciteField.setBackground(Color.WHITE);
        capaciteField.setForeground(Color.BLACK);

        // Add labels and fields to the form with the same style as the main panel
        addEventDialog.add(createLabel("Title:", new Color(235, 219, 204))); // White label
        addEventDialog.add(titreField);
        addEventDialog.add(createLabel("Description:", new Color(235, 219, 204))); // White label
        addEventDialog.add(descriptionField);
        addEventDialog.add(createLabel("Date:", new Color(235, 219, 204))); // White label
        addEventDialog.add(dateField);
        addEventDialog.add(createLabel("Location:", new Color(235, 219, 204))); // White label
        addEventDialog.add(lieuField);
        addEventDialog.add(createLabel("Type:", new Color(235, 219, 204))); // White label
        addEventDialog.add(typeField);
        addEventDialog.add(createLabel("Price:", new Color(235, 219, 204))); // White label
        addEventDialog.add(prixField);
        addEventDialog.add(createLabel("Capacity:", new Color(235, 219, 204))); // White label
        addEventDialog.add(capaciteField);

        // Add a button to validate the event addition
        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(140, 40));
        addButton.setBackground(new Color(60, 165, 92)); // Green background
        addButton.setForeground(new Color(235, 219, 204)); // White text
        addButton.setFocusPainted(false); // Remove focus border
        addButton.setFont(new Font("Arial", Font.BOLD, 16)); // Same font as main panel
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the data entered in the form
                String titre = titreField.getText();
                String description = descriptionField.getText();
                String date = dateField.getText();
                String lieu = lieuField.getText();
                String type = typeField.getText();
                String prix = prixField.getText();
                String capacite = capaciteField.getText();

                // Add the event to the database
                addEventToDatabase(titre, description, date, lieu, type, prix, capacite);

                // Close the form after addition
                addEventDialog.dispose();
            }
        });
        addEventDialog.add(addButton);

        // Set the properties of the JDialog
        addEventDialog.setSize(400, 300);
        addEventDialog.setLocationRelativeTo(this);
        addEventDialog.setVisible(true);
    }
    class DeleteButtonRenderer extends JButton implements TableCellRenderer {
        public DeleteButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class DeleteButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String eventId;
        private Component parentComponent; // Ajout d'un champ pour stocker le composant parent

        public DeleteButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Supprimer");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implement the logic to delete the corresponding event
                    deleteEvent(eventId);
                }
            });
        }
        private void deleteEvent(String eventId) {
            String url = "jdbc:mysql://localhost:3306/events";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // Créer la requête SQL de suppression
                String query = "DELETE FROM event WHERE Eventid = ?";
                
                // Préparer la déclaration SQL
                PreparedStatement statement = connection.prepareStatement(query);
                
                // Définir la valeur du paramètre dans la requête SQL
                statement.setString(1, eventId);

                // Exécuter la requête de suppression
                int rowsDeleted = statement.executeUpdate();
                
                // Vérifier si la suppression a réussi
                if (rowsDeleted > 0) {
                    System.out.println("L'événement a été supprimé avec succès !");
                    // Actualiser l'affichage si nécessaire
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Gérer les erreurs de connexion ou d'exécution de la requête
            }
        }

        private void deleteEventFromTable(int rowIndex) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String eventId = model.getValueAt(rowIndex, 0).toString(); // Obtenez l'ID de l'événement de la première colonne

            // Demander une confirmation à l'utilisateur avec le composant parent
            int option = JOptionPane.showConfirmDialog(parentComponent, "Êtes-vous sûr de vouloir supprimer cet événement ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

            // Si l'utilisateur confirme la suppression
            if (option == JOptionPane.YES_OPTION) {
                // Supprimer l'événement de la base de données
                deleteEvent(eventId);
                
                // Supprimer la ligne de la table
                model.removeRow(rowIndex);
                
                // Afficher un message de suppression réussie avec le composant parent
                JOptionPane.showMessageDialog(parentComponent, "Suppression réussie !", "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
            }
        }


      
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            eventId = (String) table.getValueAt(row, 0); // Assuming the event ID is in the first column
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
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
    private void addEventToDatabase(String titre, String description, String date, String lieu, String type, String prix, String capacite) {
        String url = "jdbc:mysql://localhost:3306/events";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Créer la requête SQL d'insertion
            String query = "INSERT INTO event (Titre, Description, Date, Lieu, Type, Prix, Capacite) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            // Préparer la déclaration SQL
            PreparedStatement statement = connection.prepareStatement(query);
            
            // Définir les valeurs des paramètres dans la requête SQL
            statement.setString(1, titre);
            statement.setString(2, description);
            statement.setString(3, date);
            statement.setString(4, lieu);
            statement.setString(5, type);
            statement.setString(6, prix);
            statement.setString(7, capacite);

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
                new Dashboard();
            }
        });
    }
}

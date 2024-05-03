package Projet;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;



public class Dashboard extends JFrame {
    private JPanel sidebarPanel;
    private JButton  ticketsButton,eventbutton, participantButton,invitebutton,statsbutton, logoutButton;
    private JPanel mainPanel;
    private Tickets TicketstContentPanel; // Define an instance of Tickets
    private Participant ParticipantContentPanel; // Define an instance of Tickets
    private Invite inviteContentPanel; // Define an instance of Tickets
   
   
   // Method to switch to event content
  
    private void switchToTicketsContent() {
        mainPanel.removeAll(); // Remove any existing components from the main panel
        mainPanel.add(TicketstContentPanel); // Add the tickets panel to the main panel
        mainPanel.revalidate(); // Revalidate the main panel to reflect the changes
        mainPanel.repaint(); // Repaint the main panel
    }
    
    // Method to switch to help content
    private void switchToParticipantContent() {
        mainPanel.removeAll(); // Remove any existing components from the main panel
        mainPanel.add(ParticipantContentPanel); // Add the help content panel to the main panel
        mainPanel.revalidate(); // Revalidate the main panel to reflect the changes
        mainPanel.repaint(); // Repaint the main panel
    }
    
 // Method to switch to invite content
    private void switchToInviteContent() {
        mainPanel.removeAll(); // Remove any existing components from the main panel
        mainPanel.add(inviteContentPanel); // Add the invite content panel to the main panel
        mainPanel.revalidate(); // Revalidate the main panel to reflect the changes
        mainPanel.repaint(); // Repaint the main panel
    }
 

    public Dashboard() {
        setTitle("Dashboard - YourEvent");
        setSize(1100, 700);
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
        JLabel titleLabel = new JLabel(" YourEvent", new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\Image\\logoEvent.png"),JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(235, 219, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons with transparent background
       
        eventbutton = createButton("EVENTS", new Color(0, 102, 51)); // Green color
        ticketsButton = createButton("TICKETS", new Color(60, 165, 92)); // Light green color
        participantButton = createButton("PARTICIPANTS", new Color(0, 102, 51)); // Green color
        invitebutton=createButton("GUESTS", new Color(0, 102, 51)); // Green color
        statsbutton=createButton("Statistics", new Color(0, 102, 51)); // Green color

        eventbutton.setIcon(new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\\\Image\\\\evenement.png"));
        ticketsButton.setIcon(new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\\\Image\\\\billets-davion.png"));
        participantButton.setIcon(new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\\\Image\\\\participation.png"));
        invitebutton.setIcon(new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\\\Image\\\\invites.png"));
        statsbutton.setIcon(new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\\\Image\\\\stats.png"));

     // Add buttons with equal margins using EmptyBorder
      
        eventbutton.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
        ticketsButton.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        participantButton.setBorder(BorderFactory.createEmptyBorder(10,45, 10, 10));
        invitebutton.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        statsbutton.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create logout button with icon
        logoutButton = new JButton("Log out", new ImageIcon("C:\\Users\\hp\\Downloads\\Image\\\\\\\\Image\\\\logout.png"));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 17));
        logoutButton.setForeground(new Color(235, 219, 204));
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
     // Add action listener to the "statistics" button
        eventbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Event event= new Event();
                 // Rendez la fenêtre visible
                 event.setVisible(true);
            }
        });
     // Create an instance of Tickets
        TicketstContentPanel = new Tickets();
        // Add action listener to the "Tickets" button
        ticketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the tickets content when the button is clicked
                switchToTicketsContent();
            }
        });
      
     
     // Create an instance of participant
        ParticipantContentPanel = new Participant();
        // Add action listener to the "Tickets" button
        participantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the tickets content when the button is clicked
                switchToParticipantContent();
            }
        });
        
     // Create an instance of invite
        inviteContentPanel = new Invite();
        invitebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToInviteContent();
            }
        });
        
     // Add action listener to the "statistics" button
        statsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 Statistics stats = new Statistics();
                 // Rendez la fenêtre visible
                 stats.setVisible(true);
            }
        });
        
        
        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between title and buttons
       // sidebarPanel.add(dashboard);
        //sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between title and buttons
        sidebarPanel.add(eventbutton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between buttons
        sidebarPanel.add(ticketsButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between buttons
        sidebarPanel.add(participantButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between buttons
        sidebarPanel.add(invitebutton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add space between buttons
        sidebarPanel.add(statsbutton);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutButton); // Add logout button at the bottom

        // Set preferred size of the sidebarPanel
        sidebarPanel.setPreferredSize(new Dimension(250, sidebarPanel.getPreferredSize().height));

        getContentPane().add(sidebarPanel, BorderLayout.WEST);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical layout
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Add title "Événements" to the main panel
        JLabel titleLabelEvents = new JLabel("Dahboard ", JLabel.CENTER);
        titleLabelEvents.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabelEvents.setForeground(new Color(60, 165, 92)); // Green color
        mainPanel.add(titleLabelEvents);

        // Création du slogan JLabel
        JLabel sloganLabel = new JLabel("**** Your event planner **** ", JLabel.CENTER);
        sloganLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Vous pouvez ajuster la police et la taille selon vos préférences

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0; // Définir le poids en y à 0 pour placer les éléments en haut
        mainPanel.add(titleLabelEvents, gbc);

        gbc.gridy++;
        mainPanel.add(sloganLabel, gbc);
        // Création d'un JPanel pour contenir le titleLabelEvents et le sloganLabel
        JPanel titlePanel = new JPanel(new GridLayout(2, 1)); // Utilisation d'un gestionnaire de disposition GridLayout avec 2 lignes et 1 colonne
        titlePanel.add(titleLabelEvents);
        titlePanel.add(sloganLabel);

        // Centrage du JPanel contenant le titre et le slogan dans le mainPanel
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(titlePanel, new GridBagConstraints());

      

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	logout();// Appel de la méthode logout() lorsque le bouton est cliqué
            }
        });
        
    
        setVisible(true);
    }
    
    private  void logout() {
         dispose();
        new LoginForm();
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

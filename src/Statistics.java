package Projet;



import javax.swing.*;
import java.awt.*;

import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class Statistics extends JPanel {
	
    public Statistics() {
        setLayout(new BorderLayout());
        // Création du dataset à partir de la base de données
        DefaultCategoryDataset dataset = createDataset();
        // Création du graphique
        JFreeChart chart = createChart(dataset);
        // Ajout du graphique à un ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setZoomOutFactor(1.0);
        chartPanel.setMouseZoomable(true);
        add(chartPanel, BorderLayout.CENTER);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Utilisation des noms complets des mois
        String[] months = new String[]{"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/events", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MONTH(date) as month, COUNT(*) as event_count FROM event GROUP BY month");
            // Création d'un tableau pour stocker le nombre d'événements par mois
            int[] eventCounts = new int[12];
            // Initialisation de toutes les valeurs à zéro
            for (int i = 0; i < eventCounts.length; i++) {
                eventCounts[i] = 0;
            }
            // Remplissage du tableau avec les données de la base de données
            while (rs.next()) {
                int monthIndex = rs.getInt("month") - 1; // Les mois sont indexés à partir de 1
                int count = rs.getInt("event_count");
                eventCounts[monthIndex] = count;
            }
            // Ajout des données au dataset
            for (int i = 0; i < eventCounts.length; i++) {
                dataset.addValue(Integer.valueOf(eventCounts[i]), "Events", months[i]);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        // Création d'un graphique à barres
        JFreeChart barChart = ChartFactory.createBarChart(
            "Events Statistics per month ",   // Titre du graphique
            "Month",                           // Label de l'axe des abscisses
            "Event",            // Label de l'axe des ordonnées
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);
        
        return barChart;
    }

    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Events Statistics");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new Statistics());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}


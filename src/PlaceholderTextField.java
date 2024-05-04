package Projet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceholderTextField extends JTextField implements FocusListener {
    private final String placeholder;
    private boolean showingPlaceholder;

    public PlaceholderTextField(final String pText) {
        super(pText);
        setFont(new Font("Arial", Font.PLAIN, 11));
        placeholder = pText;
        showingPlaceholder = true;
        addFocusListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showingPlaceholder) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // 0.5f définit la transparence à 50%
            g2d.setColor(new Color(255, 255, 255, 0));
            g2d.drawString(placeholder, getInsets().left, (getHeight() + getFont().getSize()) / 2);
            g2d.dispose(); // Libère les ressources graphiques
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (showingPlaceholder) {
            setText("");
            showingPlaceholder = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (getText().isEmpty()) {
            showingPlaceholder = true;
            repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = Math.max(size.height, getFont().getSize() + 2);
        return size;
    }
}

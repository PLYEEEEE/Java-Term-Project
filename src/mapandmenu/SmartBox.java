package mapandmenu;

import java.io.File;
import javax.swing.*;
import java.awt.*;

public class SmartBox extends JLabel {
    private String currentImagePath;
    
    public String getCurrentImagePath() {
        return currentImagePath;
    }

    public SmartBox(String text, String imgPath, int x, int y, int w, int h, int fontSize) {
        super(text, SwingConstants.CENTER);
        this.currentImagePath = imgPath;
        this.setBounds(x, y, w, h);
        this.setFont(new Font("Arial", Font.PLAIN, fontSize));
        updateAppearance(w, h);
    }

    private void updateAppearance(int w, int h) {
        if (currentImagePath != null && !currentImagePath.isEmpty() && new File(currentImagePath).exists()) {
            ImageIcon icon = new ImageIcon(currentImagePath);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            this.setIcon(new ImageIcon(img));
            this.setText(""); 
        } else {
            this.setBackground(new Color(170, 170, 170));
            this.setOpaque(true);
        }
    }
}

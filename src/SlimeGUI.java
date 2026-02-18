import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI application to test Slime functionality.
 * Provides interactive controls to test various slime operations.
 */
public class SlimeGUI extends JFrame {
    
    private Slime slime;
    private JLabel infoLabel;
    private JTextArea logArea;
    private JTextField damageField, waveField;
    
    /**
     * Creates the Slime testing GUI.
     */
    public SlimeGUI() {
        slime = new Slime(1);
        
        setTitle("Slime Tester - Interactive GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Info panel - displays current slime stats
        infoLabel = new JLabel(getSlimeInfo());
        infoLabel.setBorder(BorderFactory.createTitledBorder("Slime Stats"));
        add(infoLabel, BorderLayout.NORTH);
        
        // Log area - shows action history
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Action Log"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Control panel - buttons and input fields
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Damage input
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Damage:"), gbc);
        gbc.gridx = 1;
        damageField = new JTextField("30", 8);
        controlPanel.add(damageField, gbc);
        
        // Wave input
        gbc.gridx = 2;
        controlPanel.add(new JLabel("Wave:"), gbc);
        gbc.gridx = 3;
        waveField = new JTextField("2", 8);
        controlPanel.add(waveField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JButton attackBtn = new JButton("Attack");
        controlPanel.add(attackBtn, gbc);
        
        gbc.gridx = 1;
        JButton damageBtn = new JButton("Take Damage");
        controlPanel.add(damageBtn, gbc);
        
        gbc.gridx = 2;
        JButton waveBtn = new JButton("Update Wave");
        controlPanel.add(waveBtn, gbc);
        
        gbc.gridx = 3;
        JButton resetBtn = new JButton("Reset Slime");
        controlPanel.add(resetBtn, gbc);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Action listeners
        attackBtn.addActionListener(e -> {
            float dmg = slime.attack();
            log("Slime attacks for " + String.format("%.1f", dmg) + " damage");
            updateInfo();
        });
        
        damageBtn.addActionListener(e -> {
            try {
                float dmg = Float.parseFloat(damageField.getText().trim());
                if (dmg < 0) {
                    log("Damage cannot be negative");
                    return;
                }
                slime.takeDamage(dmg);
                log("Slime takes " + String.format("%.1f", dmg) + " damage");
                if (slime.isDead()) {
                    log("💀 Slime has died!");
                }
                updateInfo();
            } catch (NumberFormatException ex) {
                log("❌ Invalid damage value. Please enter a number.");
            }
        });
        
        waveBtn.addActionListener(e -> {
            try {
                int wave = Integer.parseInt(waveField.getText().trim());
                if (wave < 1) {
                    log("Wave must be at least 1");
                    return;
                }
                slime.updateStatsForWave(wave);
                log("Updated slime to wave " + wave);
                updateInfo();
            } catch (NumberFormatException ex) {
                log("❌ Invalid wave number. Please enter an integer.");
            }
        });
        
        resetBtn.addActionListener(e -> {
            slime = new Slime(1);
            logArea.setText("");
            log("🔄 Slime reset to level 1, wave 1");
            updateInfo();
        });
        
        // Initial log
        log("Welcome to Slime Tester!");
        log("Use the controls below to test slime functionality.");
        
        pack();
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }
    
    /**
     * Gets formatted slime information string.
     */
    private String getSlimeInfo() {
        return String.format("Level: %d | Wave: %d | HP: %.0f/%.0f | ATK: %.1f | SPD: %.1f | EXP: %d",
            slime.getLevel(), slime.getCurrentWave(), 
            slime.getCurrentHP(), slime.getMaxHP(),
            slime.getAttackDamage(), slime.getMoveSpeed(), 
            slime.getExpReward());
    }
    
    /**
     * Updates the info label with current slime stats.
     */
    private void updateInfo() {
        infoLabel.setText(getSlimeInfo());
    }
    
    /**
     * Adds a message to the log area.
     */
    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength()); // Auto scroll
    }
    
    /**
     * Main method to launch the GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlimeGUI());
    }
}
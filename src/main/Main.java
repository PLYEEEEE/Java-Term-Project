package main;
import javax.swing.*;
import java.awt.*;
import mapandmenu.*;

public class Main {
    private static CardLayout cardLayout = new CardLayout();
    private static JPanel mainPanel = new JPanel(cardLayout);
    private static GamePanel gamePanel;
    private static Color bgColor = new Color(185, 255, 140);

    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame("Knight_Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1280, 720);

        gamePanel = new GamePanel(); 
        mainPanel.add(createMenuPanel(), "MenuScene");
        mainPanel.add(createSelectCharPanel(), "SelectCharScene");
        mainPanel.add(gamePanel, "GameScene");

        window.add(mainPanel);
        window.setVisible(true);
        cardLayout.show(mainPanel, "MenuScene");

    }

    public static JPanel createMenuPanel() {
        JPanel panel = new JPanel(null); 
        panel.setBackground(bgColor);

        // --- เปลี่ยนมาใช้การสร้าง Object จากคลาสใหม่ ---
        panel.add(new SmartBox("Game Name", null, 424, 119, 431, 117, 50));
        
        SmartButton playButton = new SmartButton("Play", null, 480, 388, 320, 87);
        playButton.addActionListener(e -> cardLayout.show(mainPanel, "SelectCharScene"));
        panel.add(playButton);

        panel.add(new SmartBox("Info Left", "", 99, 280, 262, 302, 20));
        panel.add(new SmartBox("Info Right", "", 918, 280, 262, 302, 20));
        
        // ปุ่มเพิ่มเสียง
        SmartButton volUp = new SmartButton("", "src/Image/UI/Button2.png", 1100, 20, 100, 100);
        volUp.addActionListener(e -> gamePanel.getBGM().volumeUp());
        panel.add(volUp);
        volUp.addActionListener(e -> {
            gamePanel.getBGM().volumeUp();
        });
        panel.add(volUp);
        volUp.addActionListener(e -> {
            gamePanel.getBGM().volumeUp();
        });
        panel.add(volUp);

        // ปุ่มลดเสียง
        SmartButton volDown = new SmartButton("", "src/Image/UI/Button.png", 1180, 20, 100, 100);
        volDown.addActionListener(e -> gamePanel.getBGM().volumeDown());
        panel.add(volDown);
        volDown.addActionListener(e -> {
            gamePanel.getBGM().volumeDown();
        });
        panel.add(volDown);

        return panel;
    }

    public static JPanel createSelectCharPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(bgColor); 

        panel.add(new SmartBox("Game Name", null, 50, 50, 300, 80, 35));
        panel.add(new SmartBox("Stats", null, 50, 180, 300, 550, 18));

        panel.add(new SmartButton("Sword", null, 380, 50, 150, 80));
        panel.add(new SmartButton("Sword", null, 550, 50, 150, 80));
        panel.add(new SmartButton("Sword", null, 720, 50, 150, 80));

        SmartButton playBtn = new SmartButton("play", null, 1180, 50, 150, 80);
        playBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "GameScene");
            gamePanel.startGame();
            gamePanel.requestFocus(); 
        });
        panel.add(playBtn);

        panel.add(new SmartBox("Sword Title", null, 380, 180, 950, 80, 35));
        panel.add(new SmartBox("Item Details", null, 380, 280, 950, 450, 30));

        return panel;
    }

    public void gameOver() {
        cardLayout.show(mainPanel, "MenuScene");
    }
}

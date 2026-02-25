package main;
import javax.swing.*;
import java.awt.*;
import mapandmenu.*;

public class Main {
    private static CardLayout cardLayout = new CardLayout();
    private static JPanel mainPanel = new JPanel(cardLayout);
    private static GamePanel gamePanel;

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
        JPanel panel = new JPanel(null) {
        // ใช้ Main.class.getResource เพื่อดึงไฟล์จาก Classpath (ห้ามใส่ /src/ นำหน้า)
        Image bg = new ImageIcon(Main.class.getResource("/Image/MenuImage&Font/forest.jpg")).getImage();

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        try {
            // 1. โหลด Font ผ่าน getResourceAsStream เพื่อให้อ่านไฟล์จากในโปรเจกต์ได้ทุกที่
            java.io.InputStream is = Main.class.getResourceAsStream("/Image/MenuImage&Font/PressStart2P-Regular.ttf");
            if (is == null) throw new java.io.IOException("Font file not found!");
        
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
        
            // 2. ลงทะเบียน Font ในระบบ
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);

            // 3. ตั้งค่าหัวข้อเกม (ใช้ขนาด 60f ตามที่คุณต้องการ)
            JLabel title = new JLabel("FOREST LEGENDS");
            title.setFont(pixelFont.deriveFont(60f));
            title.setForeground(new Color(120, 60, 0));
            title.setBounds(250, 120, 900, 100);
            panel.add(title);

        // 4. ตั้งค่าปุ่ม Start (แนะนำให้ใช้ขนาด 30f เพื่อให้ข้อความไม่ล้นปุ่มครับ)
            JButton startBtn = new JButton("START GAME");
            startBtn.setBounds(480, 420, 320, 80);
            startBtn.setFont(pixelFont.deriveFont(25f)); // ปรับขนาดฟอนต์ให้เล็กลงสำหรับปุ่ม
            startBtn.setBackground(Color.WHITE);
            startBtn.setBorderPainted(false);
            startBtn.setFocusPainted(false);
        
            // เปลี่ยนฉากไปยังหน้าเลือกตัวละคร
            startBtn.addActionListener(e -> cardLayout.show(mainPanel, "SelectCharScene"));

            panel.add(startBtn);
    
        } catch (Exception e) {
            System.out.println("Error loading menu resources: " + e.getMessage());
            e.printStackTrace();
        }

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
        JPanel panel = new JPanel(null) {
        Image bg = new ImageIcon(Main.class.getResource("/Image/MenuImage&Font/dark_forest.jpg")).getImage();
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
        };

        try {
            java.io.InputStream is = Main.class.getResourceAsStream("/Image/MenuImage&Font/PressStart2P-Regular.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);

            // 1. หัวข้อ -> สีดำ + จัดกึ่งกลาง
            JLabel title = new JLabel("CHARACTER SELECT", SwingConstants.CENTER);
            title.setFont(baseFont.deriveFont(40f));
            title.setBackground(new Color(200, 200, 200, 220));
            title.setForeground(Color.BLACK); 
            title.setBounds(240, 40, 800, 80);
            panel.add(title);

            String[] names = {"THE MAGE", "THE ARCHER", "THE KNIGHT"};
            String[] images = {"/Image/MenuImage&Font/mage.jpg", "/Image/MenuImage&Font/archer.jpg", "/Image/MenuImage&Font/knight.jpg"};
            // อัปเดตข้อมูลตามที่คุณแก้มาครับ
            String[] statsData = {
                "Power: 18 | Speed: 4 | Magic: 20", 
                "HP : 10 | Speed: 4 | Magic: 0", 
                "Power: 22 | Speed: 6 | Magic: 2"
            };
            final int[] index = {2}; 

            JLabel characterImage = new JLabel();
            characterImage.setBounds(100, 150, 350, 500);
            panel.add(characterImage);

            // 2. กล่องชื่อ -> พื้นเทา + ตัวอักษรดำ + จัดกลาง
            JLabel nameBox = new JLabel("", SwingConstants.CENTER);
            nameBox.setBounds(500, 150, 650, 80);
            nameBox.setOpaque(true);
            nameBox.setBackground(new Color(200, 200, 200, 220)); 
            nameBox.setFont(baseFont.deriveFont(24f));
            nameBox.setForeground(Color.BLACK); 
            panel.add(nameBox);

            // 3. กล่องสเตตัส -> พื้นขาวใส + ตัวอักษรดำ + จัดกลาง
            JLabel statsLabel = new JLabel("", SwingConstants.CENTER);
            statsLabel.setBounds(500, 250, 650, 100);
            statsLabel.setOpaque(true);
            statsLabel.setBackground(new Color(255, 255, 255, 220)); 
            statsLabel.setFont(baseFont.deriveFont(16f));
            statsLabel.setForeground(Color.BLACK); 
            panel.add(statsLabel);

            Runnable updateCharacter = () -> {
                nameBox.setText(names[index[0]]);
                statsLabel.setText(statsData[index[0]]);
                Image img = new ImageIcon(Main.class.getResource(images[index[0]])).getImage();
                Image scaledImg = img.getScaledInstance(350, 500, Image.SCALE_SMOOTH);
                characterImage.setIcon(new ImageIcon(scaledImg));
            };
            updateCharacter.run();

            // 4. ปุ่ม BACK -> กล่องสีเทา + ตัวอักษรดำ + สูง 60
            JButton backBtn = new JButton("BACK");
            backBtn.setBounds(50, 50, 160, 60);
            backBtn.setFont(baseFont.deriveFont(14f));
            backBtn.setBackground(new Color(200, 200, 200, 220)); 
            backBtn.setForeground(Color.BLACK); 
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MenuScene"));
            panel.add(backBtn);

            // 5. ปุ่ม PLAY -> พื้นเขียว + ตัวอักษรขาว (โดดเด่น)
            JButton playBtn = new JButton("PLAY");
            playBtn.setBounds(950, 520, 200, 80);
            playBtn.setFont(baseFont.deriveFont(24f));
            playBtn.setBackground(new Color(34, 139, 34)); 
            playBtn.setForeground(Color.WHITE); 
            playBtn.setFocusPainted(false);
            playBtn.setBorderPainted(false);
            playBtn.addActionListener(e -> {
                cardLayout.show(mainPanel, "GameScene");
                gamePanel.startGame();
                gamePanel.requestFocus();
            });
            panel.add(playBtn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return panel;
    }

    public void gameOver() {
        cardLayout.show(mainPanel, "MenuScene");
    }
}

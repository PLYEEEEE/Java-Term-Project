package main;
import javax.swing.*;
import characters.Knight;
import characters.Slime;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning = false;
    private Knight knight;
    private List<Slime> slimes;

    private boolean up, down, left, right;

    public GamePanel() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);
        setFocusable(true);

        knight = new Knight("Knight", 350, 250);

        // สุ่มจำนวน slime 3-7 ตัว
        slimes = new ArrayList<>();
        Random rand = new Random();
        int slimeCount = 3 + rand.nextInt(5); // 3-7 ตัว
        for (int i = 0; i < slimeCount; i++) {
            Slime s = new Slime();
            // สุ่มตำแหน่ง ไม่ให้ซ้อนกับ Knight
            float sx, sy;
            do {
                sx = rand.nextInt(700) + 50;
                sy = rand.nextInt(500) + 50;
            } while (Math.abs(sx - knight.getX()) < 60 &&
                    Math.abs(sy - knight.getY()) < 60);

            s.setPosition(sx, sy);
            slimes.add(s);
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> up = true;
                    case KeyEvent.VK_S -> down = true;
                    case KeyEvent.VK_A -> {
                        left = true;
                        knight.setFacing(-1);
                    }
                    case KeyEvent.VK_D -> {
                        right = true;
                        knight.setFacing(1);
                    }
                    case KeyEvent.VK_SPACE -> {
                        knight.attack();
                        // โจมตี: ลดเลือด slime 1 หน่วยถ้าอยู่ในพื้นที่โจมตี (รัศมี 150)
                        for (Slime s : slimes) {
                            if (!s.isDead()) {
                                float dx = (s.getPositionX() + 20) - (knight.getX() + 20);
                                float dy = (s.getPositionY() + 20) - (knight.getY() + 20);
                                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                                if (dist <= 150) {
                                    s.takeDamage(1f);
                                }
                            }
                        }
                    }
                    case KeyEvent.VK_Q -> {
                        knight.useSkill();
                        // ใช้สกิล: ลดเลือด slime 2 หน่วยถ้าอยู่ในพื้นที่สกิล (รัศมี 300)
                        for (Slime s : slimes) {
                            if (!s.isDead()) {
                                float dx = (s.getPositionX() + 20) - (knight.getX() + 20);
                                float dy = (s.getPositionY() + 20) - (knight.getY() + 20);
                                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                                if (dist <= 300) {
                                    s.takeDamage(2f);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> up = false;
                    case KeyEvent.VK_S -> down = false;
                    case KeyEvent.VK_A -> left = false;
                    case KeyEvent.VK_D -> right = false;
                }
            }
        });

    }

    public void startGame() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            isRunning = true;
            gameThread.start();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        float nextX = knight.getX();
        float nextY = knight.getY();

        if (up) {
            nextY -= knight.getSpeed();
        }
        if (down) {
            nextY += knight.getSpeed();
        }
        if (left) {
            nextX -= knight.getSpeed();
        }
        if (right) {
            nextX += knight.getSpeed();
        }

        // ตรวจสอบการชนขอบหน้าจอ
        if (nextX > 0 && nextX < getWidth() - knight.getsize()) {
            knight.setX(nextX);
        }
        if (nextY > 0 && nextY < getHeight() - knight.getsize()) {
            knight.setY(nextY); 
            
        }
        knight.update();

        // ให้ทุก slime วิ่งเข้าหา knight และไม่ซ้อน
        for (Slime s : slimes) {
            if (s.isDead())
                continue;
            float slimeX = s.getPositionX() + 20;
            float slimeY = s.getPositionY() + 20;
            float knightX = knight.getX() + 20;
            float knightY = knight.getY() + 20;
            float vx = knightX - slimeX;
            float vy = knightY - slimeY;
            float dist = (float) Math.sqrt(vx * vx + vy * vy);
            float minDist = 40f;
            if (dist > minDist) {
                float speed = s.getMoveSpeed() / 60f;
                float mx = vx / dist * speed;
                float my = vy / dist * speed;
                s.move(mx, my);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Slime s : slimes) {
            s.draw(g2);
        }
        knight.draw(g2);
    }
}

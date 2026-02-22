package main;
import javax.swing.*;
import characters.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import map.*;


public class GamePanel extends JPanel implements Runnable {
    private static final int FPS = 60;
    private Knight knight;
    private List<Slime> slimes;
    private Thread gameThread;
    private boolean isRunning = false;
    private boolean up, down, left, right;
    private int sizeX = 1280;
    private int sizeY = 720;
    private float multiplier = 2.0f;
    private int col = (int)(16*multiplier);
    private int row = (int)(9*multiplier);
    private int tileSizeX = sizeX / col;
    private int tileSizeY = sizeY / row;
    private TileManagerWorld tileManager;
    private boolean isGameOver = false;
    private int maxWorldCol = 50;
    private int maxWorldRow = 50;
    private int spawnCharX = 22;
    private int spawnCharY = 9; 

    public GamePanel() {
        setPreferredSize(new Dimension(sizeX, sizeY));
        setBackground(Color.BLACK);
        setFocusable(true);

        knight = new Knight("Knight", tileSizeX*spawnCharX, tileSizeY*spawnCharY, tileSizeX, tileSizeY,sizeX,sizeY);
        tileManager = new TileManagerWorld(this , knight);


        // สุ่มจำนวน slime 3-7 ตัว
        slimes = new ArrayList<>();
        Random rand = new Random();
        int slimeCount = 3 + rand.nextInt(5); // 3-7 ตัว
        for (int i = 0; i < slimeCount; i++) {
            Slime s = new Slime( tileSizeX, tileSizeY);
            // สุ่มตำแหน่ง ไม่ให้ซ้อนกับ Knight
            int sx, sy;
            sx = rand.nextInt(maxWorldCol - 5) * tileSizeX + tileSizeX*5;
            sy = rand.nextInt(maxWorldRow - 5) * tileSizeY + tileSizeY*5;
            s.setPointsWorldPosition(sx, sy);
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
                                int posSlimeX = (int) (s.getPointsWorldX() - knight.getWorldX() + knight.screenX);
                                int posSlimeY = (int) (s.getPointsWorldY() - knight.getWorldY() + knight.screenY);
                                float dx = (posSlimeX + s.getSizeX()/2) - (knight.getScreenX() + knight.getSizeX()/2);
                                float dy = (posSlimeY + s.getSizeY()/2) - (knight.getScreenY() + knight.getSizeY()/2);
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
                                int posSlimeX = (int) (s.getPointsWorldX() - knight.getWorldX() + knight.screenX);
                                int posSlimeY = (int) (s.getPointsWorldY() - knight.getWorldY() + knight.screenY);
                                float dx = (posSlimeX + s.getSizeX()/2) - (knight.getScreenX() + knight.getSizeX()/2);
                                float dy = (posSlimeY + s.getSizeY()/2) - (knight.getScreenY() + knight.getSizeY()/2);
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
        } else {
            // รีเซ็ตสถานะเกม
            knight.reset();
            isGameOver = false;
            isRunning = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopGame() {
        isRunning = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        long nextDrawTime = System.nanoTime() + (long) drawInterval;
        while (isRunning) {
            update();
            repaint();
            try {
                long remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep(remainingTime);
                nextDrawTime += drawInterval;
            } catch (Exception e) {}
        }
    }


    private void checkCollisions() {
        long now = System.currentTimeMillis();
        for (Slime s : slimes) {
            if (!s.isDead()) {
                int posSlimeX = (int) (s.getPointsWorldX() - knight.getWorldX() + knight.screenX);
                int posSlimeY = (int) (s.getPointsWorldY() - knight.getWorldY() + knight.screenY);
                float dx = (posSlimeX + s.getSizeX() / 2) - (knight.getScreenX() + knight.getSizeX() / 2);
                float dy = (posSlimeY + s.getSizeY() / 2) - (knight.getScreenY() + knight.getSizeY() / 2);
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                float collisionDistance = (s.getSizeX() + knight.getSizeX()) / 2;

                if (dist <= collisionDistance) {
                    if (now - s.getLastDamageTime() >= 5000) {
                        knight.takeDamage(2);
                        s.setLastDamageTime(now);
                    }
                }
            }
        }
    }

    private void checkGameOver() {
        if (knight.getHealth() <= 0 && !isGameOver) {
            isGameOver = true;
            isRunning = false;
            repaint();
            // หยุดเกมและแสดงหน้าจอ Game Over
            try {
                Thread.sleep(3000); // รอ 3 วินาที
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // กลับไปที่เมนูหลัก
            isGameOver = false;
            Main main = new Main();
            // หยุดเกมและปล่อยทรัพยากร
            main.gameOver();
        }
    }

    private void update() {
        if (isGameOver) return;
        int nextX = knight.getWorldX();
        int nextY = knight.getWorldY();


        if (up)
            nextY -= knight.getSpeed();
        if (down)
            nextY += knight.getSpeed();
        if (left)
            nextX -= knight.getSpeed();
        if (right)
            nextX += knight.getSpeed();
        // เรื่อนแผนที่ยังไม่เช็คการชน ให้ตัวละครเคลื่อนที่ก่อน
        knight.setWorldX(nextX);
        knight.setWorldY(nextY);

        knight.update();
        checkCollisions();
        checkGameOver();

        //  ให้ทุก slime วิ่งเข้าหา knight และไม่ซ้อน
        for (Slime s : slimes) {
            if (s.isDead())
                continue;
            int posSlimeX = (int) (s.getPointsWorldX() - knight.getWorldX() + knight.screenX);
            int posSlimeY = (int) (s.getPointsWorldY() - knight.getWorldY() + knight.screenY);
            float dx = (posSlimeX + s.getSizeX() / 2) - (knight.getScreenX() + knight.getSizeX() / 2);
            float dy = (posSlimeY + s.getSizeY() / 2) - (knight.getScreenY() + knight.getSizeY() / 2);
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > knight.getSizeX()) {
                float moveX = (dx / dist) * s.getMoveSpeed();
                float moveY = (dy / dist) * s.getMoveSpeed();
                s.setPositionWorld((int)(s.getPointsWorldX() - moveX), (int)(s.getPointsWorldY() - moveY));
            }
        }
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        tileManager.draw(g2);
       
        for (Slime s : slimes) {
            s.draw(g2,knight);
        }
        
        if (!isGameOver && knight.getHealth() > 0) {
            knight.draw(g2);
        }
        if (isGameOver) {
            g2.setColor(new Color(0,0,0,180));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 80));
            String msg = "GAME OVER";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        }
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }
    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public float gettileSizeX() {
        return tileSizeX;
    }
    public float gettileSizeY() {
        return tileSizeY;
    }
    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }
}

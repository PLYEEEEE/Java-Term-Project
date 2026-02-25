package main;
import javax.imageio.ImageIO;
import javax.swing.*;
import characters.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import map.*;
import soundbgm.Sound;


public class GamePanel extends JPanel implements Runnable {
    private static final int FPS = 30;
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
    private int maxWorldCol = 42;
    private int maxWorldRow = 42;
    private int spawnCharX = 22;
    private int currentWave = 1;
    private Sound bgm;
    private int spawnCharY = 22;

    public Sound getBGM() {
        return bgm;
    }

    public GamePanel() {
        setPreferredSize(new Dimension(sizeX, sizeY));
        setBackground(Color.BLACK);
        setFocusable(true);

        knight = new Knight("Knight", tileSizeX*spawnCharX, tileSizeY*spawnCharY, tileSizeX, tileSizeY,sizeX,sizeY,this);
        tileManager = new TileManagerWorld(this , knight);

        bgm = new Sound();
        bgm.setFile("/res/BGM_01.wav");
        bgm.play();
        bgm.loop();

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
            s.updateStatsForWave(currentWave);
            s.setObserver(this); // ส่ง panel เป็น ImageObserver ให้ GIF animate
            slimes.add(s);
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> {
                        up = true;knight.setMove(true);
                    }
                    case KeyEvent.VK_S -> {
                        down = true;knight.setMove(true);
                    }
                    case KeyEvent.VK_A -> {
                            knight.setMove(true);
                            left = true;
                            knight.setFacing(-1);
                    }
                    case KeyEvent.VK_D -> {
                            knight.setMove(true);
                            right = true;
                            knight.setFacing(1);
                    }
                    case KeyEvent.VK_SPACE -> {
                        if (!knight.cooldownAttack) {
                            knight.attack();
                        }                      
                    }
                    case KeyEvent.VK_Q -> {
                        if (!knight.cooldownSkill) {
                            knight.useSkill();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> { up = false;
                        if (!up&&!down&&!left&&!right){
                            knight.setMove(false);
                        }              
                    }
                    case KeyEvent.VK_S -> {down = false;
                        if (!up&&!down&&!left&&!right){
                            knight.setMove(false);
                        }   
                    }
                    case KeyEvent.VK_A -> {left = false;
                        if (!up&&!down&&!left&&!right){
                            knight.setMove(false);
                        }   
                    }
                    case KeyEvent.VK_D -> {right = false;
                        if (!up&&!down&&!left&&!right){
                            knight.setMove(false);
                        }   
                    }
                }
            }
        });
    }

    public void startGame() {
        if (bgm == null) {
            bgm = new Sound();
            bgm.setFile("/res/BGM_01.wav");
            bgm.play();
            bgm.loop();
    }
        if (gameThread == null) {
            gameThread = new Thread(this);
            isRunning = true;
            gameThread.start();
        } else {
            // รีเซ็ตสถานะเกม
            currentWave = 1;
            knight.reset();
            spawnSlime();
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
                    if (now - s.getLastDamageTime() >= s.getAttackCooldown()) {
                        knight.takeDamage(2);
                        s.setState(Slime.State.ATTACKING); // trigger attack animation
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
            bgm.stop(); 
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
            bgm.play();
            // หยุดเกมและปล่อยทรัพยากร
            main.gameOver();
        }
    }

    private void update() {
        if (isGameOver) return;
        // 1. จำลองตำแหน่งถัดไป (ยังไม่ทับค่าจริง)
        int nextWorldX = knight.getWorldX();
        int nextWorldY = knight.getWorldY();
        int speed = (int) knight.getSpeed();

        if (up) nextWorldY -= speed;
        if (down) nextWorldY += speed;
        if (left) nextWorldX -= speed;
        if (right) nextWorldX += speed;

        // 2. คำนวณหาว่า "จุดกึ่งกลาง" ของตัวละครในตำแหน่งถัดไป จะไปตกที่ช่อง (Col, Row) ไหน
        // (บวก tileSize/2 เพื่อให้จุดเช็กอยู่กลางตัวละครพอดีครับ)
        int checkCol = (nextWorldX + (int)gettileSizeX() / 2) / (int)gettileSizeX();
        int checkRow = (nextWorldY + (int)gettileSizeY() / 2) / (int)gettileSizeY();
        // 3. ป้องกันดัชนีเกินขอบเขตแผนที่ (50x50)
        if (checkCol >= 1 && checkCol < getMaxWorldCol()-1 && checkRow >= 1 && checkRow < getMaxWorldRow()-1) {
            int tileNum = tileManager.mapTileNum[checkCol][checkRow];
            
            // 4. เช็กว่าช่องนั้น "เดินได้" หรือไม่
            if (tileManager.tile[tileNum].collision == false) {
                // ถ้าไม่ชน ถึงจะอนุญาตให้เปลี่ยนพิกัดจริงครับ
                knight.setWorldX(nextWorldX);
                knight.setWorldY(nextWorldY);
            }
        }

        knight.update();
        checkCollisions();
        checkGameOver();

        //  ให้ทุก slime วิ่งเข้าหา knight และไม่ซ้อน
        for (Slime s : slimes) {
            if (s.isDead()) {continue;

            }

            int posSlimeX = (int) (s.getPointsWorldX() - knight.getWorldX() + knight.screenX);
            int posSlimeY = (int) (s.getPointsWorldY() - knight.getWorldY() + knight.screenY);
            float dx = (posSlimeX + s.getSizeX() / 2) - (knight.getScreenX() + knight.getSizeX() / 2);
            float dy = (posSlimeY + s.getSizeY() / 2) - (knight.getScreenY() + knight.getSizeY() / 2);
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            boolean slimeMoved = false;
            if (dist > knight.getSizeX() && !s.iFrame) {
                float moveX = (dx / dist) * s.getMoveSpeed();
                float moveY = (dy / dist) * s.getMoveSpeed();
                s.setPositionWorld((int)(s.getPointsWorldX() - moveX), (int)(s.getPointsWorldY() - moveY));
                // กำหนดทิศที่ slime หันหน้าไป (ไปทางซ้ายหรือขวา)
                s.setFacing(dx < 0 ? -1 : 1);
                slimeMoved = true;
            }
            // แจ้ง animation ว่า slime กำลังเดินหรือหยุด
            s.notifyMoving(slimeMoved);

            if(knight.usingSkill){
                if (!s.isDead()&&!s.iFrame&&dist <= knight.getSkillRage()/2) {
                    s.iFrame = true;
                    s.iFrameStart = System.currentTimeMillis();
                    s.takeDamage(2f);
                }          
            }
            if (knight.attacking&&!s.iFrame){
                if (!s.isDead()&&!s.iFrame&&dist <= knight.getAttackRage()/2) {
                    s.iFrame = true;
                    s.iFrameStart = System.currentTimeMillis();
                    s.takeDamage(1f);                    
                } 
            }
            s.upDateIFrame();
            // อัปเดต state machine ของ animation
            s.updateState();
        }
        
        boolean allDead = true; 
        for (Slime s : slimes) {
            if (!s.isDead()) {
                allDead = false;
                break;
            }
        }

        if (allDead) {
            System.out.println("All slimes dead, increasing wave to " + (currentWave + 1));
            currentWave++;
            spawnSlime();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        try {
            Image imagegrass = ImageIO.read(getClass().getResourceAsStream("/Image/Terrain/Grass/Grass2.png"));
            g2.drawImage(imagegrass, 0, 0, sizeX, sizeY, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        tileManager.draw(g2);
       
        for (Slime s : slimes) {
            s.draw(g2,knight);
        }
        
        if (!isGameOver && knight.getHealth() > 0) {
            knight.draw(g2);
        }

        System.out.println("Painting wave: " + currentWave);

        // วาด Wave ที่มุมขวา
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String waveText = "Wave: " + currentWave;
        int textX = sizeX - g2.getFontMetrics().stringWidth(waveText) - 10;
        int textY = 30;
        g2.drawString(waveText, textX, textY);

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

    public void spawnSlime(){
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
            s.updateStatsForWave(currentWave);
            s.setObserver(this); // ส่ง panel เป็น ImageObserver ให้ GIF animate
            slimes.add(s);
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
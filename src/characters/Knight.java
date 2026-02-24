package characters;
import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Knight extends Character {

    private int health = 10;
    private final int maxHealth = 10;
    public int screenX;
    public int screenY;

    private boolean action = false;
    private boolean move = false;
    private ImageCharacter imageIdle[];
    private ImageCharacter imageMove[];
    private int moveCount = 0;
    private int idleCount = 0;
    private int delayMunti = 4;
    private int delayAni = 0;


    private float attackRange = 150;
    private float skillRange = 300;
    public boolean attacking = false;
    public boolean usingSkill = false;
    public boolean cooldownAttack = false;
    public boolean cooldownSkill = false;
    private long attackStart;
    private long attackCooldown = 400;
    private long skillStart;
    private long skillCooldown = 10000;
    private int facing = 1; // 1 = right, -1 = left
    private int iFrameDuration = 500;
    public boolean iFrame = false;
    public long iFrameStart;
    
    public Knight(String name, int worldX, int worldY, int sizex, int sizey, int screenWidth, int screenHeight) {
        super(name, 5, worldX, worldY, 4.0f, sizex, sizey);
        this.screenX = screenWidth / 2 - sizex / 2;
        this.screenY = screenHeight / 2 - sizey / 2;
        imageMove = new ImageCharacter[6];
        imageIdle = new ImageCharacter[5];
        loadImageIdle();
        loadImageMove();
    }

    public void setFacing(int dir) {
        facing = dir;
    }

    public void attack() {
        if (!usingSkill & !attacking && !cooldownAttack) {
            cooldownAttack = true;
            attacking = true;
            action = true;
            attackStart = System.currentTimeMillis();
        }
    }

    public void useSkill() {
        if (!attacking && !usingSkill && !cooldownSkill) {
            cooldownSkill = true;
            action = true;
            usingSkill = true;
            skillStart = System.currentTimeMillis();
        }
    }

    public void update() {
        long now = System.currentTimeMillis();

        if (attacking && now - attackStart > 100) {
            attacking = false;
            action = false;
        }
        if (iFrame && now - iFrameStart >= iFrameDuration) {
            iFrame = false;
        }
        if (usingSkill && now - skillStart > 600) {
            usingSkill = false;
            action = false;
        }
        if(cooldownAttack&&now-attackStart>attackCooldown) {
            cooldownAttack = false;
        }
        if (cooldownSkill&&now-skillStart>skillCooldown) {
            cooldownSkill = false;
        }
    }

    public void draw(Graphics2D g2) {
        // ตัวละคร
        if (!move && action == false && facing == 1) {
            g2.drawImage(imageIdle[idleCount].image,(int)(screenX-sizeX*3/4),(int)(screenY-sizeY*1.5),(int)(sizeX*2.5),(int)(sizeY*2.5), null);
            if (delayAni == delayMunti) {
                delayAni = -1;
                idleCount++;
                if (idleCount==5) {
                    idleCount=0;
                }
            }
            delayAni++;
        } else if (!move && action == false && facing == -1) { // เมื่อหันซ้าย
            int x = (int)(screenX - sizeX * 3 / 4);
            int y = (int)(screenY - sizeY * 1.5);
            int width = (int)(sizeX * 2.5);
            int height = (int)(sizeY * 2.5);
            int imgW = imageIdle[idleCount].image.getWidth(null);
            int imgH = imageIdle[idleCount].image.getHeight(null);

            // ใช้การสลับ x1 (x+width) กับ x2 (x) เพื่อให้รูปกลับด้าน
            g2.drawImage(imageIdle[idleCount].image, x + width, y, x, y + height, 0, 0, imgW, imgH, null);

            if (delayAni == delayMunti) {
                delayAni = -1;
                idleCount++;
                if (idleCount == 5) {
                    idleCount = 0;
                }
            }
            delayAni++;
        } else if(move && !action && facing == 1){
            g2.drawImage(imageMove[moveCount].image,(int)(screenX-sizeX*3/4),(int)(screenY-sizeY*1.5),(int)(sizeX*2.5),(int)(sizeY*2.5), null);
            if (delayAni == delayMunti) {
                delayAni = -1;
                moveCount++;
                if (moveCount==6) {
                    moveCount=0;
                }
            }
            delayAni++;
        } else if (move && action == false && facing == -1) { // เมื่อหันซ้าย
            int x = (int)(screenX - sizeX * 3 / 4);
            int y = (int)(screenY - sizeY * 1.5);
            int width = (int)(sizeX * 2.5);
            int height = (int)(sizeY * 2.5);
            int imgW = imageMove[moveCount].image.getWidth(null);
            int imgH = imageMove[moveCount].image.getHeight(null);

            // ใช้การสลับ x1 (x+width) กับ x2 (x) เพื่อให้รูปกลับด้าน
            g2.drawImage(imageMove[moveCount].image, x + width, y, x, y + height, 0, 0, imgW, imgH, null);

            if (delayAni == delayMunti) {
                delayAni = -1;
                moveCount++;
                if (moveCount == 6) {
                    moveCount = 0;
                }
            }
            delayAni++;
        }
      
        // โจมตีครึ่งวงกลม
        if (attacking) {
            g2.setColor(new Color(255, 0, 0, 120));

            int startAngle = (facing == 1) ? -90 : 90;
            g2.fillArc(
                    (int)(screenX - attackRange/2 + getSizeX()/2),
                    (int)(screenY - attackRange/2 + getSizeY()/2),
                    (int)attackRange,
                    (int)attackRange,
                    startAngle,
                    180
            );
        }

        // สกิล 360
        if (usingSkill) {
            g2.setColor(new Color(0, 255, 255, 120));
            g2.fillOval(
                    (int)(screenX - skillRange/2 + getSizeX()/2),
                    (int)(screenY - skillRange/2 + getSizeY()/2),
                    (int)skillRange,
                    (int)skillRange
            );
        }

        int barWidth = (int)450;
        int barHeight = 30;
        int barX = 50;
        int barY = 35;
        float healthPercent = (float)health / maxHealth;
        g2.setColor(Color.black);
        g2.fillRect(barX, barY, barWidth, barHeight);
        g2.setColor(Color.GREEN);
        if (iFrame) {
            g2.setColor(Color.blue);
        }
        g2.fillRect(barX + 1, barY + 1, (int)((barWidth - 2) * healthPercent), barHeight - 2);
        
        //ดูCoolDownSkill
        long nowSkill = System.currentTimeMillis();
        int sizeSkillCooldown = 100;
        int posXSkillCooldown = 1130;
        int posYSkillCooldown = 570;
        float valueSkill = (float)(nowSkill - skillStart) / skillCooldown;
        if (valueSkill >= 1) {
            valueSkill = 1;
        }
        g2.setColor(Color.black);
        g2.fillRect(posXSkillCooldown,posYSkillCooldown, sizeSkillCooldown, sizeSkillCooldown);
        g2.setColor(Color.green);
        g2.fillRect(posXSkillCooldown+1,posYSkillCooldown+1,sizeSkillCooldown-2,sizeSkillCooldown-2);
        if (skillStart!=0) {
           g2.setColor(Color.gray);
           g2.fillRect(posXSkillCooldown+1,((posYSkillCooldown+2)+(int)((sizeSkillCooldown-2)*(valueSkill))),sizeSkillCooldown-2,(int)((sizeSkillCooldown-2)*(1-valueSkill)));  
        }
        //ดูNormalattack
        long nowAttack = System.currentTimeMillis();
        int sizeAttackCooldown = 100;
        int posXAttackCooldown = 980;
        int posYAttackCooldown = 570;
        float valueAttack = (float)(nowAttack - attackStart) / attackCooldown;
        if (valueAttack >= 1) {
            valueAttack = 1;
        }
        g2.setColor(Color.black);
        g2.fillRect(posXAttackCooldown,posYAttackCooldown, sizeAttackCooldown, sizeAttackCooldown);
        g2.setColor(Color.red);
        g2.fillRect(posXAttackCooldown+1,posYAttackCooldown+1,sizeAttackCooldown-2,sizeAttackCooldown-2);
        if (attackStart!=0) {
           g2.setColor(Color.gray);
           g2.fillRect(posXAttackCooldown+1,((posYAttackCooldown+2)+(int)((sizeAttackCooldown-2)*(valueAttack))),sizeAttackCooldown-2,(int)((sizeAttackCooldown-2)*(1-valueAttack)));  
        }
    }


    public void loadImageIdle(){
        try {
            
            imageIdle[0] = new ImageCharacter();
            imageIdle[0].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Idle/MCIdle1.png"));
            
            imageIdle[1] = new ImageCharacter();
            imageIdle[1].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Idle/MCIdle2.png"));

            imageIdle[2] = new ImageCharacter();
            imageIdle[2].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Idle/MCIdle3.png"));

            imageIdle[3] = new ImageCharacter();
            imageIdle[3].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Idle/MCIdle4.png"));

            imageIdle[4] = new ImageCharacter();
            imageIdle[4].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Idle/MCIdle5.png"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadImageMove(){
        try {
            
            imageMove[0] = new ImageCharacter();
            imageMove[0].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Moving/MCMoving1.png"));
            
            imageMove[1] = new ImageCharacter();
            imageMove[1].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Moving/MCMoving2.png"));

            imageMove[2] = new ImageCharacter();
            imageMove[2].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Moving/MCMoving3.png"));

            imageMove[3] = new ImageCharacter();
            imageMove[3].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Moving/MCMoving4.png"));

            imageMove[4] = new ImageCharacter();
            imageMove[4].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Moving/MCMoving5.png"));

            imageMove[5] = new ImageCharacter();
            imageMove[5].image = ImageIO.read(getClass().getResourceAsStream("/Image/Main_Character/MC/Moving/MCMoving6.png"));
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void reset() {
        this.health = maxHealth;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        this.iFrame = true;
        iFrameStart = System.currentTimeMillis();
        if (this.health < 0) {
            this.health = 0;
        }
    }
    
    public boolean getMove(){
        return move;
    }
    public boolean getAction(){
        return action;
    }
    public void setMove(boolean move){
        this.move = move;
    }
    public void setAction(boolean action){
        this.action = action;
    }

    public float getSkillRage(){
        return skillRange;
    }
    public float getAttackRage(){
        return attackRange;
    }
    public float getSizeX() {
        return sizeX;
    }
    public float getSizeY() {
        return sizeY;
    }

    public int getHealth() {
        return health;
    }

    public int getScreenX() {
        return screenX;
    }
    public int getScreenY() {
        return screenY;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

}

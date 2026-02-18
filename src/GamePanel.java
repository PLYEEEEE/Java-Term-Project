import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private Knight knight;

    private boolean up, down, left, right;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);

        knight = new Knight("Knight", 350, 250);

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
                    case KeyEvent.VK_SPACE -> knight.attack();
                    case KeyEvent.VK_Q -> knight.useSkill();
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

        Timer timer = new Timer(16, e -> {
            update();
            repaint();
        });
        timer.start();
    }

    private void update() {

        float dx = 0, dy = 0;

        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        knight.move(dx, dy);
        knight.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        knight.draw((Graphics2D) g);
    }
}

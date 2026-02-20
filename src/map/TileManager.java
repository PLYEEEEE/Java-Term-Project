package map;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.getCol()][gp.getRow()];
        getTileImage();
        loadMap("src/map/map.txt");
    }

    public void getTileImage() {
        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Image/Terrain/Grass/Grass1.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Tree/Tree1.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Tree/Tree2.png"));
            tile[2].collision = true;
            
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Tree/Tree3.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Bush/Bush1.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Bush/Bush2.png"));
            tile[5].collision = true;

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Rock/Rock1.png"));
            tile[6].collision = true;

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/Image/Environment/Rock/Rock2.png"));
            tile[7].collision = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String filePath) {
        // โค้ดสำหรับโหลดแผนที่จากไฟล์ (ถ้ามี)
        try {
            // ตัวอย่างการโหลดแผนที่จากไฟล์ (สามารถปรับแต่งตามรูปแบบไฟล์ของคุณ)
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < gp.getRow()) {
                String[] numbers = line.split(" ");
                for (int col = 0; col < numbers.length && col < gp.getCol(); col++) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        int col = 0;
        int row = 0;
        while (col < gp.getCol() && row < gp.getRow()) {
            int tileNum = mapTileNum[col][row];
            if (tileNum > 0 ) {
              g2.drawImage(tile[0].image, (int)(col * gp.gettileSizeX()), (int)(row * gp.gettileSizeY()), (int)gp.gettileSizeX(), (int)gp.gettileSizeY(), null);
            }
            
            g2.drawImage(tile[tileNum].image, (int)(col * gp.gettileSizeX()), (int)(row * gp.gettileSizeY()), (int)gp.gettileSizeX(), (int)gp.gettileSizeY(), null);
            col++;
            if (col == gp.getCol()) {
                col = 0;
                row++;
            }
        }
    }
}

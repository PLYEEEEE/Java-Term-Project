package map;

import main.GamePanel;
import characters.Knight;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class TileManagerWorld {
    GamePanel gp;
    Knight knight;
    Tile[] tile;
    int mapTileNum[][];

    public TileManagerWorld(GamePanel gp, Knight knight) {
        this.gp = gp;
        this.knight = knight;
        tile = new Tile[20];
        mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        getTileImage();
        loadMap("src/map/world.txt");
    }

    public void getTileImage() {
        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Image/Terrain/Grass/Grass1.png"));

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/Image/Terrain/Grass/Grass2.png"));

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/Image/Terrain/Grass/GrassPath1.png"));

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
            while ((line = br.readLine()) != null && row < gp.getMaxWorldRow()) {
                String[] numbers = line.split(" ");
                for (int col = 0; col < numbers.length && col < gp.getMaxWorldCol(); col++) {
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

        for(int row = 0; row < gp.getMaxWorldRow(); row++){
            for(int col = 0; col < gp.getMaxWorldCol(); col++){

                int ScreenX = (int)(col * gp.gettileSizeX() - knight.getWorldX()+ knight.screenX );
                int ScreenY = (int)(row * gp.gettileSizeY() - knight.getWorldY() + knight.screenY );
                int tileNum = mapTileNum[col][row];

                if(tileNum == 0){
                    g2.drawImage(tile[0].image, ScreenX, ScreenY,
                    (int)gp.gettileSizeX(),
                    (int)gp.gettileSizeY(), null);
                }else if (tileNum == 8 ||tileNum == 9){
                    g2.drawImage(tile[tileNum].image, ScreenX, ScreenY,
                    (int)gp.gettileSizeX(),
                    (int)gp.gettileSizeY(), null);
                }else{
                    g2.drawImage(tile[0].image, ScreenX, ScreenY,
                    (int)gp.gettileSizeX(),
                    (int)gp.gettileSizeY(), null);
                }
            }
        }

        for(int row = 0; row < gp.getMaxWorldRow(); row++){
            for(int col = 0; col < gp.getMaxWorldCol(); col++){

                int ScreenX = (int)(col * gp.gettileSizeX() - knight.getWorldX() + knight.screenX );
                int ScreenY = (int)(row * gp.gettileSizeY() - knight.getWorldY() + knight.screenY );
                int tileNum = mapTileNum[col][row];

            // ถ้าเป็นพื้นอย่างเดียว ไม่ต้องวาดซ้ำ
                if(tileNum == 0 || tileNum == 8 || tileNum == 9) continue;

                int width = (int)gp.gettileSizeX();
                int height = (int)gp.gettileSizeY();

                if(tileNum == 1){
                    width *= 3;
                    height *= 3;

                    ScreenY -= gp.gettileSizeY();
                }else if(tileNum == 2){
                    width *= 2;
                    height *= 2;

                    ScreenY -= gp.gettileSizeY();
                }else if(tileNum == 3){
                    width *= 4;
                    height *= 4;

                    ScreenY -= gp.gettileSizeY();
                }

                g2.drawImage(tile[tileNum].image, ScreenX, ScreenY, width, height, null);
            }
        }
        
    }
}

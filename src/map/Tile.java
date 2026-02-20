package map;

import java.awt.image.BufferedImage; // ต้อง Import ตัวนี้เพื่อเก็บรูปภาพ

public class Tile {
    public BufferedImage image; 
    public boolean collision = false; 

    public int widthScale = 1;
    public int heightScale = 1;
}
package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity
{
    public int score;
    GamePanel gp;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public String direction;
    public BufferedImage treeIcon;
    public BufferedImage clockIcon;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public boolean collisionOn = false;
    public boolean attacking = false;
    public boolean freeRoam = false;

    public int maxLife;
    public int life;

    public BufferedImage setup(String imagePath, int width, int height)
    {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath))); // Throws error if resource is null
            image = uTool.scaleImage(image, width, height);
            System.out.println("Loaded sprite: " + imagePath);
        }
        catch (NullPointerException e)
        {
            System.out.println("Error: Could not find file: " + imagePath);
        }
        catch (IOException e)
        {
            System.out.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
        return image;
    }
}

package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity
{
    GamePanel gp;
    KeyHandler keyH;
    Graphics2D g2;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public int timer = 5;
    long lastTimerUpdate = System.nanoTime();
    boolean gameOver = false;

    public Player (GamePanel gp, KeyHandler keyH)
    {
        this.gp = gp;
        this.keyH = keyH;
        this.g2 = g2;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        loadTreeIcon();
        loadClockIcon();
    }

    public int getScore()
    {
        return score;
    }
    public void setDefaultValues()
    {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 24;
        speed = 6;
        direction = "down";

        //Player status
        maxLife = 6;
        life = maxLife;
    }

    public void resetGame()
    {
        setDefaultValues();
        timer = 45;
        score = 0;
        life = maxLife;
        attacking = false;
        gameOver = false;
        lastTimerUpdate = System.nanoTime();
    }

    public void getPlayerImage()
    {
        up1 = setup("/player/joe_up_1.png", gp.tileSize, gp.tileSize);
        up2 = setup("/player/joe_up_2.png", gp.tileSize, gp.tileSize);
        down1 = setup("/player/joe_down_1.png", gp.tileSize, gp.tileSize);
        down2 = setup("/player/joe_down_2.png", gp.tileSize, gp.tileSize);
        left1 = setup("/player/joe_left_1.png", gp.tileSize, gp.tileSize);
        left2 = setup("/player/joe_left_2.png", gp.tileSize, gp.tileSize);
        right1 = setup("/player/joe_right_1.png", gp.tileSize, gp.tileSize);
        right2 = setup("/player/joe_right_2.png", gp.tileSize, gp.tileSize);
    }
    public void getPlayerAttackImage()
    {
        attackUp1 = setup("/player/joe_attack_up_1.png", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("/player/joe_attack_up_2.png", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("/player/joe_attack_down_1.png", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("/player/joe_attack_down_2.png", gp.tileSize, gp.tileSize*2);
        attackLeft1 = setup("/player/joe_attack_left_1.png", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/player/joe_attack_left_2.png", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/player/joe_right_attack_1.png", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/player/joe_attack_right_2.png", gp.tileSize * 2, gp.tileSize);
    }
    public void loadTreeIcon()
    {
        treeIcon = setup("/player/tree_icon.png", gp.tileSize, gp.tileSize);
    }
    public void loadClockIcon()
    {
        clockIcon = setup("/player/clock_icon.png", gp.tileSize, gp.tileSize);
    }
    int attackDuration = 15;
    int attackCounter = 0;

    public void update()
    {
        if (!gameOver)
        {
            if (keyH.attackPressed && !attacking) {
                attacking = true;
                attacking();
                spriteCounter = 0; // Reset attack animation counter
                System.out.println("Attacking: " + attacking + ", Direction: " + direction + ", SpriteNum: " + spriteNum);
            }
            if (attacking == true)
            {
                attackCounter++;
                if (attackCounter > attackDuration)
                {
                    attacking = false;
                    attackCounter = 0;
                }
            }
            else {
                attacking = false;
            }

            if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
                if (keyH.upPressed) {
                    direction = "up";
                } else if (keyH.downPressed) {
                    direction = "down";
                } else if (keyH.leftPressed) {
                    direction = "left";
                } else if (keyH.rightPressed) {
                    direction = "right";
                }

                collisionOn = false;
                gp.cChecker.checkTile(this);

                if (collisionOn == false)
                {
                    switch (direction)
                    {
                        case "up":
                            worldY -= speed;
                            break;
                        case "down":
                            worldY += speed;
                            break;
                        case "left":
                            worldX -= speed;
                            break;
                        case "right":
                            worldX += speed;
                            break;
                    }
                }
                spriteCounter++;
                if (spriteCounter > 10) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            }

            if (gp.gameState == gp.playState && gp.gameState != gp.freeRoamState)
            {
                long currentTime = System.nanoTime();
                if ((currentTime - lastTimerUpdate) >= 1_000_000_000)
                {
                    timer--;
                    lastTimerUpdate = currentTime;

                    if (timer <= 0) {
                        gameOver = true;
                        gp.gameState = gp.gameOverState;
                    }
                }
            }
        }
    }

    public void attacking() {
        if (!gameOver)
        {
            spriteCounter++;

            if (spriteCounter <= 5) {
                spriteNum = 1;
            }
            if (spriteCounter > 5 && spriteCounter <= 25) {
                spriteNum = 2;

                // Tree Destruction Logic
                int attackTileX = worldX;
                int attackTileY = worldY;

                // Determine the attack area based on direction
                switch (direction) {
                    case "up":
                        attackTileX = worldX + solidArea.x + (solidArea.width / 2);
                        attackTileY = worldY + solidArea.y - gp.tileSize; // 1 tile above
                        break;
                    case "down":
                        attackTileX = worldX + solidArea.x + (solidArea.width / 2);
                        attackTileY = worldY + solidArea.y + solidArea.height + gp.tileSize; // 1 tile below
                        break;
                    case "left":
                        attackTileX = worldX + solidArea.x - gp.tileSize; // 1 tile left
                        attackTileY = worldY + solidArea.y + (solidArea.height / 2);
                        break;
                    case "right":
                        attackTileX = worldX + solidArea.x + solidArea.width + gp.tileSize; // 1 tile right
                        attackTileY = worldY + solidArea.y + (solidArea.height / 2);
                        break;
                }

                // Convert the attack position to tile indices
                int col = attackTileX / gp.tileSize;
                int row = attackTileY / gp.tileSize;

                // Check if the tile exists and is a tree
                if (col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow) {
                    int tileNum = gp.tileM.mapTileNum[col][row];

                    if (tileNum == 4) {     // If it's a tree
                        gp.tileM.mapTileNum[col][row] = 3;
                        score++;
                        System.out.println("Tree destroyed at (" + col + ", " + row + "). Score: " + score + ".");
                    }
                    if (tileNum == 7)
                    {
                        gp.tileM.mapTileNum[col][row] = 3;
                        score += 3;
                        System.out.println("Tree destroyed at (" + col + ", " + row + "). Score: " + score + ".");
                    }
                    if (tileNum == 8)
                    {
                        gp.tileM.mapTileNum[col][row] = 3;
                        score += 10;
                        System.out.println("Tree destroyed at (" + col + ", " + row + "). Score: " + score + ".");
                    }
                }
            }
            if (spriteCounter > 25) {
                spriteNum = 1;
                spriteCounter = 0;
                attacking = false;
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        BufferedImage img = null;
        if (!gameOver)
        {
            switch (direction)
            {
                case "up":
                    if (attacking == false)
                    {
                        if (spriteNum == 1)
                        {
                            img = up1;
                        }
                        if (spriteNum == 2)
                        {
                            img = up2;
                        }
                    }
                    if (attacking == true)
                    {
                        if (spriteNum == 1)
                        {
                            img = attackUp1;
                        }
                        if (spriteNum == 2)
                        {
                            img = attackUp2;
                        }
                    }
                    break;
                case "down":
                    if (attacking == false)
                    {
                        if (spriteNum == 1)
                        {
                            img = down1;
                        }
                        if (spriteNum == 2)
                        {
                            img = down2;
                        }
                    }
                    if (attacking == true)
                    {
                        if (spriteNum == 1)
                        {
                            img = attackDown1;
                        }
                        if (spriteNum == 2)
                        {
                            img = attackDown2;
                        }
                    }
                    break;
                case "left":
                    if (attacking == false)
                    {
                        if (spriteNum == 1)
                        {
                            img = left1;
                        }
                        if (spriteNum == 2)
                        {
                            img = left2;
                        }
                    }
                    if (attacking == true)
                    {
                        if (spriteNum == 1)
                        {
                            img = attackLeft1;
                        }
                        if (spriteNum == 2)
                        {
                            img = attackLeft2;
                        }
                    }
                    break;
                case "right":
                    if (attacking == false)
                    {
                        if (spriteNum == 1)
                        {
                            img = right1;
                        }
                        if (spriteNum == 2)
                        {
                            img = right2;
                        }
                    }
                    if (attacking == true)
                    {
                        if (spriteNum == 1)
                        {
                            img = attackRight1;
                        }
                        if (spriteNum == 2)
                        {
                            img = attackRight2;
                        }
                    }
                    break;


            }

            if (img == null)
            {
                System.out.println("Error: Image is null for direction = " + direction +
                        ", attacking = " + attacking + ", spriteNum = " + spriteNum);
            }
            g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
            int padding = 60;
            int iconX = gp.screenWidth - gp.tileSize - padding;
            int iconY = 10;

            g2.drawImage(treeIcon, iconX, iconY, gp.tileSize, gp.tileSize, null);

            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.setColor(Color.WHITE);
            g2.drawString("x " + score, iconX + gp.tileSize + 10, iconY + (gp.tileSize / 2) + 10);

            if (gp.gameState == gp.playState)
            {
                int padding2 = 20;
                int clockX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                int clockY = 20;

                // Draw the clock icon
                g2.drawImage(clockIcon, clockX, clockY, gp.tileSize, gp.tileSize, null);

                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.setColor(Color.WHITE);
                if (timer <= 10) {
                    g2.setColor(Color.RED);
                }
                g2.drawString(String.valueOf(timer), clockX + gp.tileSize + 10, clockY + (gp.tileSize / 2) + 12);
            }
        }
    }
}

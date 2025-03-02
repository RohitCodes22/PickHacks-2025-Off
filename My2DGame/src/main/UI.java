package main;
import entity.Player;
import object.OBJ_Heart;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.io.InputStream;

public class UI
{
    GamePanel gp;
    Graphics2D g2;
    BufferedImage heart_full, heart_half, heart_blank;
    Font arial_40, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public int commandNum = 0;

    double playTime;
    DecimalFormat df = new DecimalFormat("#0.00");

    public UI (GamePanel gp)
    {
        this.gp = gp;
        this.gp.player = gp.player;

        arial_40 = new Font("Sans-Serif", Font.PLAIN, 40);
        arial_80B = new Font("Sans-Serif", Font.BOLD, 80);

        SuperObject heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text)
    {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2)
    {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        if (gp.gameState == gp.titleState)
        {
            drawTitleScreen();
        }
        if (gp.gameState == gp.playState || gp.gameState == gp.freeRoamState)
        {
            drawPlayerLife();
        }
        if (gp.gameState == gp.pauseState)
        {
            drawPlayerLife();
            drawPauseScreen();
        }
        if (gp.gameState == gp.gameOverState && gp.gameState != gp.freeRoamState)
        {
            drawGameOverScreen();
        }
    }

    public void drawGameOverScreen()
    {
        int playerImgWidth = gp.tileSize * 2;
        int playerImgHeight = gp.tileSize * 2;
        int imgX = (gp.screenWidth - playerImgWidth) / 2;
        int imgY = (gp.screenHeight - playerImgHeight) / 3; // Place the image closer to the top
        g2.drawImage(gp.player.down1, imgX, imgY, playerImgWidth, playerImgHeight, null);

        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(Color.RED);
        String gameOverText = "Game Over";
        int gameOverTextWidth = g2.getFontMetrics().stringWidth(gameOverText); // Measure text width
        int gameOverX = (gp.screenWidth - gameOverTextWidth) / 2;
        int gameOverY = imgY + playerImgHeight + 50; // Offset a bit below the image
        g2.drawString(gameOverText, gameOverX, gameOverY);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        String finalScoreText = "Final Score: " + gp.player.getScore() + " points";
        int finalScoreTextWidth = g2.getFontMetrics().stringWidth(finalScoreText); // Measure text width
        int finalScoreX = (gp.screenWidth - finalScoreTextWidth) / 2;
        int finalScoreY = gameOverY + 40; // Offset a bit below the "Game Over" text
        g2.drawString(finalScoreText, finalScoreX, finalScoreY);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        String text3 = "Return to Title Screen";
        int x = getXForCenteredText(text3);
        int y = finalScoreY + 50;
        g2.drawString(text3, x, y);
        if (commandNum == 0)
        {
            g2.drawString(">", x-gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        String text2 = "Quit";
        x = getXForCenteredText(text2);
        y += 50;
        g2.drawString(text2, x, y);
        if (commandNum == 1)
        {
            g2.drawString(">", x-gp.tileSize, y);
        }

    }

    public void drawPlayerLife()
    {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        while (i < gp.player.maxLife/2)
        {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        while (i < gp.player.life)
        {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life)
            {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    public void drawTitleScreen()
    {
        g2.setColor(new Color(0, 91, 75));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "Miner Quest: Adventure";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;

        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(new Color(200, 160, 35));
        g2.drawString(text, x, y);

        x = gp.screenWidth/2 - gp.tileSize*2/2;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        text = "Timed Challenge";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if (commandNum == 0)
        {
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "Free Roam";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1)
        {
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "Quit";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2)
        {
            g2.drawString(">", x-gp.tileSize, y);
        }
    }

    public void drawPauseScreen() {
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 4;

        // Draw the pause header
        g2.drawString(text, x, y);

        // Draw the options
        String[] options = {
                "Press Pause Trigger to Resume",
                "Return to Title Screen"
        };

        for (int i = 0; i < options.length; i++) {
            text = options[i];
            x = getXForCenteredText(text);
            y += gp.tileSize + (i == 0 ? 50 : 0);

            g2.drawString(text, x, y);

            // Draw the cursor (">") if this option is selected
            if (commandNum == i) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }
    }


    public int getXForCenteredText(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}

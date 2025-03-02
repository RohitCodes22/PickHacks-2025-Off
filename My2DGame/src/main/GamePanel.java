package main;

import entity.Player;
import tile.TileManager;

import java.awt.*;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.lang.Thread;

public class GamePanel extends JPanel implements Runnable
{
    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 30;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    int FPS = 60;

    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;


    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;
    public final int freeRoamState = 4;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.blue);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.requestFocus();
    }

    public void setupGame()
    {
        gameState = titleState;
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run()
    {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null)
        {
            update();
            repaint();
            try
            {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0)
                {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void resetGame()
    {
        player.resetGame();
        tileM.resetMap();
    }

    public void update()
    {
        if (gameState == playState || gameState == freeRoamState)
        {
            player.update();
        }
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (gameState == titleState)
        {
            ui.draw(g2);
        }
        else if (gameState == playState || gameState == freeRoamState|| gameState == pauseState)
        {
            tileM.draw(g2);
            player.draw(g2);
            ui.draw(g2);
        }
        else if (gameState == gameOverState)
        {
            ui.draw(g2);
        }

        if (gameState == titleState)
        {
            resetGame();
        }

        g2.dispose();
    }
}

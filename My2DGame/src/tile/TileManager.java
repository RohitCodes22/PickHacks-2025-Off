package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

public class TileManager
{
    GamePanel gp;
    public Tile[ ] tile;
    public int[][] mapTileNum;
    boolean destroyTree = false;

    public TileManager (GamePanel gp)
    {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage()
    {
        setup(0, "grass01 (1)", false);
        setup(1, "wall (1)", true);
        setup(2, "water01 (1)", true);
        setup(3, "earth", false);
        setup(4, "tree", true);
        setup(5, "034", false);
        setup(6, "033", false);
        setup(7, "silver", true);
        setup(8, "ruby", true);
    }

    public void setup(int index, String imagePath, boolean collision)
    {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath)
    {
        try
        {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow)
            {
                String line = br.readLine();
                while (col < gp.maxWorldCol && row < gp.maxWorldRow)
                {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == gp.maxWorldCol)
                {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch(Exception e)
        {

        }
    }

    public void destroyTree() {
        // Calculate player's hitbox in the map's grid
        int playerLeftWorldX = gp.player.worldX + gp.player.solidArea.x;
        int playerRightWorldX = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width;
        int playerTopWorldY = gp.player.worldY + gp.player.solidArea.y;
        int playerBottomWorldY = gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height;

        // Convert player's world coordinates to tile indices
        int playerLeftCol = playerLeftWorldX / gp.tileSize;
        int playerRightCol = playerRightWorldX / gp.tileSize;
        int playerTopRow = playerTopWorldY / gp.tileSize;
        int playerBottomRow = playerBottomWorldY / gp.tileSize;

        // Check all four corners of the player's hitbox
        int[] cols = { playerLeftCol, playerRightCol };
        int[] rows = { playerTopRow, playerBottomRow };

        for (int col : cols) {
            for (int row : rows) {
                // If the tile is a tree (e.g., index 4), replace it with dirt (e.g., index 3)
                if ((mapTileNum[col][row] == 4) && gp.player.attacking) {
                    mapTileNum[col][row] = 3;  // Replace tree tile with dirt
                    System.out.println("Tree destroyed at (" + col + ", " + row + ")");
                }
                if ((mapTileNum[col][row] == 7) && gp.player.attacking) {
                    mapTileNum[col][row] = 3;  // Replace tree tile with dirt
                    System.out.println("Tree destroyed at (" + col + ", " + row + ")");
                }
                if ((mapTileNum[col][row] == 8) && gp.player.attacking) {
                    mapTileNum[col][row] = 3;  // Replace tree tile with dirt
                    System.out.println("Tree destroyed at (" + col + ", " + row + ")");
                }
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow)
        {
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY,null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol)
            {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void resetMap()
    {
        loadMap("/maps/world01.txt");
    }
}

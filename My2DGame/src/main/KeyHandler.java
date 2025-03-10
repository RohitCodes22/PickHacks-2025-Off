package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{
    public boolean attackPressed;
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean restartPressed;

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        System.out.println("Key pressed: " + KeyEvent.getKeyText(code));
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNum == 1) {
                    gp.gameState = gp.freeRoamState;
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        if (gp.gameState == gp.gameOverState) {
                if (code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 1;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 1) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_SPACE) {
                    if (gp.ui.commandNum == 0) {
                        gp.gameState = gp.titleState;
                    }
                    if (gp.ui.commandNum == 1) {
                        System.exit(0);
                    }
                }
        }
        if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.freeRoamState;
                }
                if (gp.ui.commandNum == 1) {
                    gp.gameState = gp.titleState;
                }
            }
        }
            if (gp.gameState == gp.playState) {
                if (code == KeyEvent.VK_SPACE) {
                    attackPressed = true;
                }
                if (code == KeyEvent.VK_W) {
                    upPressed = true;
                }
                if (code == KeyEvent.VK_S) {
                    downPressed = true;
                }
                if (code == KeyEvent.VK_A) {
                    leftPressed = true;
                }
                if (code == KeyEvent.VK_D) {
                    rightPressed = true;
                }
            }


            if (gp.gameState == gp.freeRoamState) {
                if (code == KeyEvent.VK_SPACE) {
                    attackPressed = true;
                }
                if (code == KeyEvent.VK_W) {
                    upPressed = true;
                }
                if (code == KeyEvent.VK_S) {
                    downPressed = true;
                }
                if (code == KeyEvent.VK_A) {
                    leftPressed = true;
                }
                if (code == KeyEvent.VK_D) {
                    rightPressed = true;
                }
                if (code == KeyEvent.VK_P) {
                    gp.gameState = gp.pauseState;
                }
            }

        }

        @Override
        public void keyReleased (KeyEvent e) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_SPACE) {
                attackPressed = false;
            }
            if (code == KeyEvent.VK_W) {
                upPressed = false;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = false;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = false;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = false;
            }

        }
    }

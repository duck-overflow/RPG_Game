package de.duckoverflow.rpg.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    // Debug
    boolean checkDrawTime = false;
    GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        // Movement
        exchangeBooleanTypes(keyCode, true);

        // Pause
        if (keyCode == KeyEvent.VK_P) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }

        // Debug
        if (keyCode == KeyEvent.VK_T) {
            checkDrawTime = !checkDrawTime;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        exchangeBooleanTypes(keyCode, false);

    }

    private void exchangeBooleanTypes(int keyCode, boolean value) {
        if (keyCode == KeyEvent.VK_W) upPressed = value;
        if (keyCode == KeyEvent.VK_A) leftPressed = value;
        if (keyCode == KeyEvent.VK_S) downPressed = value;
        if (keyCode == KeyEvent.VK_D) rightPressed = value;
    }
}

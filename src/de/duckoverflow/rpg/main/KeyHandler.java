package de.duckoverflow.rpg.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        exchangeBooleanTypes(e, true);

    }

    @Override
    public void keyReleased(KeyEvent e) {

        exchangeBooleanTypes(e, false);

    }

    private void exchangeBooleanTypes(KeyEvent e, boolean value) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = value;
        if (code == KeyEvent.VK_A) leftPressed = value;
        if (code == KeyEvent.VK_S) downPressed = value;
        if (code == KeyEvent.VK_D) rightPressed = value;

    }
}

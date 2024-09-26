package de.duckoverflow.rpg.main;

import de.duckoverflow.rpg.entity.Player;
import de.duckoverflow.rpg.object.SuperObject;
import de.duckoverflow.rpg.tile.TileManager;

import javax.swing.*;
import java.awt.*;

/**
 * The Game Panel.
 */
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16 x 16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int fps = 60;

    // System
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSettter = new AssetSetter(this);
    Sound music = new Sound();
    //Sound effect = new Sound();
    public UI ui = new UI(this);

    // Entity and Objects
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10];

    // Game State
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    /**
     * GamePanel constructor.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    /**
     * Game Setup place initial Objects.
     */
    public void setupGame() {
        aSettter.setObject();
        playMusic(0);
        gameState = playState;
        //temporary
        stopMusic();

    }

    /**
     * Start the game loop.
     */
    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    /**
     * Game Loop of the Game Thread.
     */
    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / fps; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS - " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Update game information.
      */
    public void update() {

        if (gameState == playState) {
            player.update();
        }
/*        if (gameState == pauseState) {
            // pause game
        }
*/
    }

    /**
     * Draw things on JPanel.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Debug
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // Tile
        tileM.draw(g2);

        // Object
        for (SuperObject superObject : obj) {
            if (superObject != null) superObject.draw(g2, this);
        }

        // Player
        player.draw(g2);

        // UI

        ui.draw(g2);

        // Debug
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.WHITE);
            g2.drawString("Draw Time: " + passed + " ms", 10, 400);
            System.out.println("Draw Time: " + passed + " ms");
        }

        g2.dispose();
    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();

    }

    public void stopMusic() {
        music.stop();
    }
/*
    public void playSoundEffect(int i) {

        effect.setFile(i);
        effect.play();

    }
*/
}

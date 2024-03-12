package com.example;

import java.awt.Graphics;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoopThread;
    private final int FPS = 130;
    private final int UPS = 200;

    private Playing playing;
    private Menu menu;
    
    public final static int TILES_DEFAULT = 32;
    public final static float SCALE = 2f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int)(TILES_DEFAULT * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        initializeClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initializeClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    public void update() {
        switch(Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch(Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }


    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS;
        double timePerUpdate = 1000000000.0 / UPS;

        long previousTime = System.nanoTime();


        int frameCount = 0;
        long lastChecked = System.currentTimeMillis();
        long updateCount = 0;

        double deltaU = 0;
        double deltaF = 0;

        while(true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updateCount++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
                frameCount++;
            }
        
            if(System.currentTimeMillis() - lastChecked >= 1000) {
                lastChecked = System.currentTimeMillis();
                System.out.println("FPS - " + frameCount + " | UPS - " + updateCount);
                frameCount = 0;
                updateCount = 0;
            }
        }
    }

    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING) {
            playing.getPlayer().resetDirectionBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}


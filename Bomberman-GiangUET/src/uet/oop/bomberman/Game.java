package uet.oop.bomberman;

import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.input.Keyboard;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.sound.sampled.Clip;
import uet.oop.bomberman.music.Music;
import uet.oop.bomberman.music.Sound;

/**
 * Tạo vòng lặp cho game, lưu trữ một vài tham số cấu hình toàn cục, Gọi phương
 * thức render(), update() cho tất cả các entity
 */
public class Game extends Canvas {

    public static final double VERSION = 1.0;
    public static final int TILES_SIZE = 16,
            WIDTH = TILES_SIZE * (31 / 2),
            HEIGHT = 13 * TILES_SIZE;

    public static int SCALE = 3;

    public static final String TITLE = "BoomberMan-NguyễnGiangUET";

    private static final int BOMBRATE = 1;
    private static final int BOMBRADIUS = 1;
    private static final double BOMBERSPEED = 1.0;

    public static final int TIME = 200;
    public static final int POINTS = 0;
    public static final int LIVES = 3;

    protected static int SCREENDELAY = 5;
    protected static int WAITSTART = 19;
    protected static int WAITTIME = 22;

    public static int bombRate = BOMBRATE;
    protected static int bombRadius = BOMBRADIUS;
    public static double bomberSpeed = BOMBERSPEED;

    protected int _screenDelay = SCREENDELAY;
    public static int _waitTime = 23;
    public static int _waitStart = WAITSTART;

    private final Keyboard _input;
    private boolean _running = false;
    private boolean _paused = true;
    private boolean _win = false;

    private final Board _board;
    private final Screen screen;
    private final Frame _frame;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public Game(Frame frame) {
        new Music();
        _frame = frame;
        _frame.setTitle(TITLE);

        screen = new Screen(WIDTH, HEIGHT);
        _input = new Keyboard();

        _board = new Board(this, _input, screen);
        addKeyListener(_input);
    }

    private void renderGame() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        _board.render(screen);

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen._pixels[i];
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        _board.renderMessages(g);

        g.dispose();
        bs.show();
    }

    private void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        Graphics g = bs.getDrawGraphics();

        _board.drawScreen(g);

        g.dispose();
        bs.show();
    }

    private void update() {
        _input.update();
        _board.update();
    }

    public void start() throws InterruptedException {
        _running = true;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0; //nanosecond, 60 frames per second
        double delta = 0;
        int frames = 0;
        int updates = 0;
        requestFocus();
        while (_running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }

            if (_paused) {
                if (/*_board._levelLoader.getLevel() == 1 && */_screenDelay <= 0) {
                    _waitTime = 23;
                    Music.clipTheme.setMicrosecondPosition(0);
                    Music.clipTheme.start();
                    Music.clipNextLevel.stop();
                    Music.clipGameOver.stop();
                    Music.clipTheme.loop(Clip.LOOP_CONTINUOUSLY);
                }

                if (_board.getShow() == 4 && _input.enter) {
                    _waitStart = 0;
                }
                if (_board.getShow() == 2 && _screenDelay == SCREENDELAY) {
                    _board.setShow(2);
                    Music.clipScreen.stop();

                    Thread.sleep(500);

                    Music.clipNextLevel.setMicrosecondPosition(0);
                    Music.clipNextLevel.start();

                    Music.clipTheme.stop();
                    Music.clipGameOver.stop();
                }

                if (_waitTime == WAITTIME) {
                    Music.clipTheme.stop();
                    Music.clipGameOver.setMicrosecondPosition(0);
                    Music.clipGameOver.start();

                }

                if (_board.getShow() == 1 && _input.backSpace) {
                    _waitTime = 0;
                }

                if (_waitStart <= 0 && _board.getShow() == 4) {
                    _board.setShow(2);
                }

                if (_waitTime <= 0) {
                    Music.clipGameOver.stop();
                    _board.setShow(1);
                }

                if (_screenDelay <= 0) {
                    _board.setShow(-1);
                    _paused = false;
                }

                renderScreen();
            } else {
                renderGame();
            }

            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                _frame.setTime(_board.subtractTime());
                _frame.setPoints(_board.getPoints());
                _frame.setLives(_board.getLives());
                timer += 1000;
                _frame.setTitle(TITLE + " | " + updates + " rate, " + frames + " fps");
                updates = 0;
                frames = 0;

                if (_board.getShow() == 2) {
                    --_screenDelay;
                }
                if (_board.getShow() == 1) {
                    --_waitTime;
                }
                if (_board.getShow() == 4) {
                    --_waitStart;
                }
            }
        }
    }

    /*
	|--------------------------------------------------------------------------
	| Getters & Setters
	|--------------------------------------------------------------------------
     */
    public void run() {
        _running = true;
        _paused = false;
    }

    public void stop() {
        _running = false;
    }

    public boolean isRunning() {
        return _running;
    }

    public static double getBomberSpeed() {
        return bomberSpeed;
    }

    public static int getBombRate() {
        return bombRate;
    }

    public static int getBombRadius() {
        return bombRadius;
    }

    public static void addBomberSpeed(double i) {
        bomberSpeed += i;
    }

    public static void addBombRadius(int i) {
        bombRadius += i;
    }

    public static void addBombRate(int i) {
        bombRate += i;
    }

    public void resetScreenDelay() {
        _screenDelay = SCREENDELAY;
    }

    public Board getBoard() {
        return _board;
    }

    public boolean isPaused() {
        return _paused;
    }

    public void pause() {
        _paused = true;
    }

    public void win() {
        _running = false;
        _win = true;
    }

    public static void addPlayerSpeed(double i) {
        bomberSpeed += i;
    }

    public static double getPlayerSpeed() {
        return bomberSpeed;
    }

    public Keyboard getInput() {
        return _input;
    }

}

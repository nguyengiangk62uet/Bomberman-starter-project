package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.music.Music;
import uet.oop.bomberman.music.Sound;

public class Portal extends Tile {
    
    protected Board _board;
    
    public Portal(int x, int y, Board board, Sprite sprite) {
        super(x, y, sprite);
        _board = board;
    }
    
    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi vào
        if (e instanceof Bomber) {
            if (Game.getBombRate() < Bomber._bombRateNow) {
                Game.addBombRate(1);
            }
            if (_board.detectNoEnemies() == false) {
                return false;
            }
            try {
                if (e.getXTile() == getX() && e.getYTile() == getY()) {
                    if (_board.detectNoEnemies()) {
                        _board.nextLevel();
                        Sound nextLv = new Sound("res\\audio\\power04.wav");
                        nextLv.play();
                        Music.clipNextLevel.stop();
                        Music.clipScreen.stop();
                        Music.clipTheme.stop();
                    }
                }
            } catch (Exception ex) {
                System.out.println("WIN");
                _board.winGame();
            }
            
            return true;
        }
        return false;
    }
}

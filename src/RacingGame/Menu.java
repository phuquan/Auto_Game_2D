package RacingGame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.*;


/**
 * Created by Hong-PC on 11/15/2015.
 */
public class Menu extends BasicGameState{

    Image playNow;
    Image exitGame;
    Image backgound;

    public Menu(int state) {

    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        playNow = new Image("res/start.png");
        exitGame = new Image("res/exit.png");
        backgound = new Image("res/background.jpg");
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        backgound.draw(0,0);
        graphics.drawString("RACING GAME", 350, 50);
        playNow.draw(350, 150);
        exitGame.draw(350, 400);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        int posX = Mouse.getX();
        int posY = Mouse.getY();
        //System.out.println("X: " + posX + "Y: "+posY);

        if ((posX > 350 && posX < 450) && ( posY > 350 && posY < 450)) {
            if (Mouse.isButtonDown(0)) {
                stateBasedGame.enterState(1);
            }
        }

        if ((posX > 350 && posX < 450) && ( posY > 100 && posY < 200)) {
            if (Mouse.isButtonDown(0)) {
                System.exit(0);
            }
        }
    }

    @Override
    public int getID() {
        return 0;
    }

}

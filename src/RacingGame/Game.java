package RacingGame;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Hong-PC on 11/14/2015.
 */
public class Game extends StateBasedGame {

    public static final String gameName = "Racing Game";
    public static final int menu = 0;
    public static final int play = 1;

    public Game(String gameName) {
        super(gameName);
        this.addState(new Menu(menu));
        this.addState(new Play(play));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(menu).init(gameContainer, this);
        this.getState(play).init(gameContainer, this);
        this.enterState(menu);
    }


}

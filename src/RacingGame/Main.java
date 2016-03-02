package RacingGame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {

    public static void main(String[] args) {
        AppGameContainer appGameContainer;

        try {
            appGameContainer = new AppGameContainer(new Game(Game.gameName));
            appGameContainer.setDisplayMode(800, 600, false);
            //appGameContainer.setTargetFrameRate(200);
            appGameContainer.setAlwaysRender(true);
            appGameContainer.start();
        }catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

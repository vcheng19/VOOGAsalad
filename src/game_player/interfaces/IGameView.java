package game_player.interfaces;

import game_engine.GameEngineInterface;
import game_engine.game_elements.Unit;
import game_player.UnitViews.UnitImageView;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import main.IMainView;


/**
 * This interface specifies the different methods needed to run and animate the game elements.
 * Throws exception when towers/units are placed in wrong locations.
 * 
 * @author ownzandy
 *
 */

public interface IGameView {

    void playGame (int gameIndex);

    void restartGame ();

    void toggleGame ();

    void changeColorScheme (int colorIndex);

    void changeGameSpeed (double gameSpeed);

    GameEngineInterface getGameEngine ();

    void setSpecificUnitIsSelected (Unit unit);
    
    void setUnitToPlace (String name);
    
    void hideHUD();

    void updateHUD (UnitImageView view); 
    
    void setCanPlaceUnit(boolean canPlaceUnit);
    
    Scene getScene();
    
    IMainView getMainView();
    
    void setCSS(String fileName);
    
    MediaPlayer getMusic();
    
}

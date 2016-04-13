package game_player;

/**
 * This interface specifies the different methods needed to run and animate the game elements.
 * Throws exception when towers/units are placed in wrong locations.
 * @author ownzandy
 *
 */

public interface IGameView {
    
    //event handler that begins the animation and movement of elements
    void playGame(int gameIndex);
    
    //event handler that toggles the game
    void toggleGame();
    
    void changeColorScheme (int colorIndex);
      
    void changeGameSpeed (int gameSpeed); 
    
}

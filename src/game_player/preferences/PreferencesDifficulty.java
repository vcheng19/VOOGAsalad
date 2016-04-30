package game_player.preferences;

import java.util.ResourceBundle;

import game_player.GameDataSource;
import game_player.interfaces.IGUIObject;
import game_player.interfaces.IGameView;
import game_player.view.GUIComboBox;
import game_player.view.PlayerGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class PreferencesDifficulty extends GUIComboBox {
	
	private static final int PANEL_SPACING = 10;
	private static final int VISIBLE_ROW_COUNT = 3;
	private static final int COMBOBOX_WIDTH = 100;
	
	private ResourceBundle myResources;
	private GameDataSource myGameData;
	private IGameView myView;
	private ObservableList<String> options;
	private ComboBox<String> comboBox;
	private Button comboBoxButton;
	
	public PreferencesDifficulty(ResourceBundle r, GameDataSource gameData, IGameView view, PlayerGUI GUI) {
    	super(r, gameData, view, r.getString("DifficultyLabel"), r.getString("DifficultyOptions"), GUI);
        myResources = r;
        myGameData = gameData;
        myView = view;
    }
	
	public void performAction() {
		
	}

}

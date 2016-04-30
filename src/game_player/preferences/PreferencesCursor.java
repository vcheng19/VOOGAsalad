package game_player.preferences;

import java.util.ResourceBundle;

import game_player.GameDataSource;
import game_player.interfaces.IGameView;
import game_player.view.GUIComboBox;
import game_player.view.PlayerGUI;
import javafx.collections.ObservableList;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class PreferencesCursor extends GUIComboBox {
	
	private ResourceBundle myResources;
	private GameDataSource myGameData;
	private IGameView myView;
	private Scene myScene;
	
	public PreferencesCursor(ResourceBundle r, GameDataSource gameData, IGameView view, PlayerGUI GUI) {
		super(r, gameData, view, r.getString("CursorLabel"), GUI);
        myResources = r;
        myGameData = gameData;
        myView = view;
        myScene = view.getScene();
    }
	
	public ObservableList<String> populateOptions(ObservableList<String> list) {
		String[] difficulties = myResources.getString("CursorOptions").trim().split(",");
		for (String s: difficulties) {
			list.add(s);
		}
		return list;
	}
	
	public void performAction() {
		myScene.setCursor(new ImageCursor(new Image(super.getComboBox().getValue())));
	}
}
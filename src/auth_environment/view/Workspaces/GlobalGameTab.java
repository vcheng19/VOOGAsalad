package auth_environment.view.Workspaces;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import auth_environment.backend.GameDataController;
import auth_environment.backend.ISelector;
import auth_environment.backend.SelectorModel;
import auth_environment.delegatesAndFactories.DragDelegate;
import auth_environment.delegatesAndFactories.FileChooserDelegate;
import auth_environment.delegatesAndFactories.NodeFactory;
import auth_environment.view.ElementPicker;
import auth_environment.view.MapDisplay;
import auth_environment.view.Menus.MenuToolBar;
import game_data.GameData;
import game_engine.game_elements.Tower;

import auth_environment.backend.ISettings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by BrianLin on 3/31/16.
 * Team member responsible: Brian
 *
 * This class represents a single tab (ie Level) within our View.
 */

public class GlobalGameTab implements IWorkspace {

	private static final String DIMENSIONS_PACKAGE = "auth_environment/properties/dimensions";
	private ResourceBundle myDimensionsBundle = ResourceBundle.getBundle(DIMENSIONS_PACKAGE);
	
	private static final String NAMES_PACKAGE = "auth_environment/properties/names";
	private ResourceBundle myNamesBundle = ResourceBundle.getBundle(NAMES_PACKAGE);
	
	private static final String URLS_PACKAGE = "auth_environment/properties/urls";
	private ResourceBundle myURLSBundle = ResourceBundle.getBundle(URLS_PACKAGE);

	// TODO: replace with EngineWorkspace class
	private GameData gameData; // This originates here via XML 
//	private GameDataController myGameDataController; // TODO: find this
	private NodeFactory myNodeFactory = new NodeFactory(); 
	
	private BorderPane myBorderPane = new BorderPane(); 
	private TextField myGameNameField;
	
	public GlobalGameTab() {
		this.gameData = new GameData();
//		myPicker = new ElementPicker();
//		this.myGameDataController = new GameDataController(gameData, myPicker, myDisplay.getGrid().getPathGraphFactory());
		this.setupBorderPane();
	}

	private void setupBorderPane() {
		
		this.myBorderPane.setPrefSize(Double.parseDouble(myDimensionsBundle.getString("defaultBorderPaneWidth")),
				Double.parseDouble(myDimensionsBundle.getString("defaultBorderPaneHeight")));
		
		this.myBorderPane.setCenter(this.buildCenter());
		
	}
	
	private Node buildCenter() {
		VBox center = myNodeFactory.buildVBox(Double.parseDouble(myDimensionsBundle.getString("defaultVBoxSpacing")), 
				Double.parseDouble(myDimensionsBundle.getString("defaultVBoxPadding")));
		center.getChildren().addAll(this.buildWompImage(),
				myNodeFactory.centerNode(this.buildTextInput()),
				myNodeFactory.centerNode(this.buildSplashChooser()));
		return center; 
	}
	
	private HBox buildWompImage() {
		return myNodeFactory.centerNode(myNodeFactory.buildImageView(myNamesBundle.getString("wompWelcomeImage")));
	}
	
	private HBox buildTextInput() {
		this.myGameNameField = myNodeFactory.buildTextFieldWithPrompt(myNamesBundle.getString("gameNamePrompt"));
		this.myGameNameField.setOnAction(e -> this.submitButtonPressed(this.myGameNameField));
		
		Button submitNameButton = myNodeFactory.buildButton(myNamesBundle.getString("submitButtonLabel"));
		submitNameButton.setOnAction(e -> this.submitButtonPressed(this.myGameNameField));
		
		HBox hb = myNodeFactory.centerNode(this.myGameNameField);
		hb.getChildren().add(submitNameButton);
		return hb;
	}
	
	private HBox buildSplashChooser() {
		Button splashButton = myNodeFactory.buildButton(myNamesBundle.getString("chooseSplashLabel"));
		splashButton.setOnAction(e -> this.buildSplashChooser());
		return myNodeFactory.centerNode(splashButton);
	}

	//	public void writeToGameData() {
	////		gameData.setLevels(myPicker.getLevels());
	//		gameData.setEnemies(myPicker.getEnemies());
	//		gameData.setTerrains(myPicker.getTerrains());
	//		gameData.setTowerTypes(myPicker.getTowers());
	//		gameData.setPaths(myDisplay.getGrid().getPathGraphFactory().getPaths());
	//	}
	
	// TODO: exctract these methods to the GlobalGameModel class
	private void submitButtonPressed(TextField input) {
		if (checkValidInput(input)) {
			this.gameData.getSettings().setName(input.getText());
			input.clear();
		}
	}
	
	// TODO: put in Model
	private void chooseSplash() {
		FileChooserDelegate fileChooser = new FileChooserDelegate(); 
		File splash = fileChooser.chooseImage(myNamesBundle.getString("chooseSplashLabel"));
		// TODO: store File
	}
	
	private boolean checkValidInput(TextField input) {
		return input.getText().length() > 0; 
	}
	
	@Override
	public Node getRoot() {
		return this.myBorderPane; 
	}
}
package game_player.view;

import game_player.GameDataSource;
import game_player.interfaces.IGUIObject;
import game_player.interfaces.IGameView;
import game_player.interfaces.IPlayerTab;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import game_engine.GameEngineInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import main.IMainView;

public class PlayerMainTab implements IPlayerTab {
    
	private static final int NUMBER_OF_PLAY_CYCLES = -1;
    private static final int TOP_PADDING = 5;
    private static final int RIGHT_PADDING = 5;
    private static final int BOTTOM_PADDING = 5;
    private static final int LEFT_PADDING = 5;
    private static final String GUI_ELEMENTS = "game_player/resources/GUIElements";
    private static final String PACKAGE_NAME = "game_player.";
    private static final int PANEL_PADDING = 10;
    private static final int CONFIGURATION_PANEL_PADDING = 20;
    private Tab myTab;
    private BorderPane myRoot;
    private ResourceBundle myResources;
    private ResourceBundle elementsResources;
    private GameDataSource gameData;
    private List<IGUIObject> gameElements;
    private IGameView gameView;
    private GameCanvas myCanvas;
    private GameHUD myHUD;
    private Scene myScene;
    private IMainView myMainView;
    private String tabName;
    private MediaPlayer myMusic;
    private VBox gameSection;
    private VBox configurationPanel;
    private VBox gameMenu;
    private VBox gamePanel;
    private VBox towerPanel;

    private GameEngineInterface gameEngine;

    public PlayerMainTab (GameEngineInterface engine,
                          ResourceBundle r,
                          Scene scene,
                          IMainView main,
                          String tabName) {
        this.gameEngine = engine;
        this.myResources = r;
        this.gameElements = new ArrayList<>();
        this.elementsResources = ResourceBundle.getBundle(GUI_ELEMENTS);
        this.gameData = new GameDataSource();
        this.myScene = scene;
        this.myMainView = main;
        this.tabName = tabName;
        this.gameData.setDoubleValue("High Score", 0);
    }

    @Override
    public Tab getTab () {
        myTab = new Tab();
        myRoot = new BorderPane();
        setMusic(myResources.getString("Audio"));
        createUISections();
        initializeCanvas();
        initializeElements();
        placeUISections();
        myTab.setContent(myRoot);
        myTab.setText(tabName);
        return myTab;
    }

    private void initializeCanvas () {
        myCanvas = new GameCanvas(myResources);
        myHUD = new GameHUD(myResources);
        gameSection.getChildren().addAll(myCanvas.createCanvas(), myHUD.createNode());
        gameView = new GameView(gameEngine, myCanvas, myHUD, myScene, this);
        gameView.playGame(0);
    }

    private void initializeElements () {
        IGUIObject newElement = null;
        Enumeration<String> resourceKeys = elementsResources.getKeys();
        while (resourceKeys.hasMoreElements()) {
            String currentKey = resourceKeys.nextElement();
            String[] keyAndPosition = elementsResources.getObject(currentKey).toString().split(",");
            try {
                newElement = (IGUIObject) Class.forName(PACKAGE_NAME + keyAndPosition[0].trim())
                        .getConstructor(ResourceBundle.class, GameDataSource.class, IGameView.class)
                        .newInstance(myResources, gameData, gameView);
                gameElements.add(newElement);

                placeElement(newElement, keyAndPosition[1].trim());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void placeElement (IGUIObject element, String position) {
        try {
            getClass().getDeclaredMethod(position, Node.class).invoke(this, element.createNode());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUISections () {
        gameSection = new VBox();
        configurationPanel = new VBox(CONFIGURATION_PANEL_PADDING);
        gameMenu = new VBox(PANEL_PADDING);
        gamePanel = new VBox(PANEL_PADDING);
        towerPanel = new VBox(PANEL_PADDING);
        this.configurePanels();
    }

    private void placeUISections () {
        myRoot.setCenter(gameSection);
        myRoot.setRight(configurationPanel);
        myRoot.setTop(gameMenu);
        myRoot.setBottom(gamePanel);
        myRoot.setLeft(towerPanel);
    }

    private void configurePanels () {
    	Insets panelInsets = new Insets(TOP_PADDING, RIGHT_PADDING, BOTTOM_PADDING,
    	        LEFT_PADDING);
        Label configurationLabel = new Label(myResources.getString("Configuration"));
        configurationLabel.setFont(new Font("Arial", 20));
        configurationPanel.setAlignment(Pos.TOP_CENTER);
//        configurationPanel.getChildren().add(configurationLabel);
        configurationPanel.setPadding(panelInsets);
        gamePanel.setPadding(panelInsets);
        towerPanel.setPadding(panelInsets);
        gamePanel.getStyleClass().add("hbox");
    }

    protected void updateGameElements () {
        for (IGUIObject object : gameElements) {
            object.updateNode();
        }
    }

    protected void addToTowerPanel (Node element) {
        towerPanel.getChildren().add(element);
    }

    protected void addToTop (Node element) {
        gameMenu.getChildren().add(element);
    }

    protected void addToConfigurationPanel (Node element) {
        configurationPanel.getChildren().add(element);
    }

    public void removeFromConfigurationPanel (Node element) {
        configurationPanel.getChildren().remove(element);
    }

    protected void addToGamePanel (Node element) {
        gamePanel.getChildren().add(element);
    }
    
    protected IMainView getMainView() {
    	return myMainView;
    }
    
    public void setMusic(String name) {
		myMusic = new MediaPlayer(new Media(new File(name).toURI().toString()));
		myMusic.setCycleCount(NUMBER_OF_PLAY_CYCLES);
		myMusic.setAutoPlay(true);
    }
    
    public MediaPlayer getMusic() {
    	return myMusic;
    }
}

package game_player.view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import game_engine.GameEngineInterface;
import game_engine.affectors.Affector;
import game_engine.game_elements.Unit;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class GameHUD {
	
	private static final int PANEL_SPACING = 5;

    private ResourceBundle myResources;
    private ImageView unitImage;
    private List<ImageView> children;
    private List<Button> upgrades;
    private Button sellButton;
    private Label unitType;
    private HBox HUDBox;
    private VBox upgradesBox;
    private GameEngineInterface engine;
    private IGameView gameView;

    public GameHUD (ResourceBundle r) {
        myResources = r;
        upgrades = new ArrayList<>();
        children = new ArrayList<>();
        upgradesBox = new VBox(PANEL_SPACING);
    }
    
    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
    }

    public void setEngine (GameEngineInterface engine) {
        this.engine = engine;
    }

    public Node createNode () {
        HUDBox = new HBox();
        addToHUD();
        whenNothingSelected();
        return HUDBox;
    }

    public void addToHUD () {
    	HUDBox.getChildren().clear();
        VBox unitStuff = new VBox();
        unitStuff.setAlignment(Pos.CENTER);
        unitImage = new ImageView();
        unitType = new Label();
        sellButton = new Button();
        unitStuff.getChildren().addAll(unitType, unitImage, sellButton);
        HUDBox.getChildren().addAll(unitStuff, upgradesBox);
        upgradesBox.setAlignment(Pos.CENTER);
        HUDBox.setAlignment(Pos.CENTER_LEFT);
    }

    public void whenTowerSelected (Unit tower) {
    	addToHUD();
        gameView.setSpecificUnitIsClicked(tower);
        unitType.setText(tower.getName());
        unitImage.setImage(new Image(tower.toString() + ".png"));
        removeUpgrades();
        addUpgrades(tower, new HBox(PANEL_SPACING));
        addChildrenUpgrades(tower);
        sellButton.setText(myResources.getString("HUDSellButton"));
        sellButton.setOnMouseClicked(e -> engine.sellUnit(tower));
    }
    
    public <E> void removeUpgrades() {
//    	for (int i = 0; i < upgradesList.size(); i++) {
//            HUDBox.getChildren().remove(upgradesList.get(i));
//        }
    	upgrades.clear();
    	children.clear();
    	upgradesBox.getChildren().clear();
    }

    public void addUpgrades (Unit u, HBox upgradeBox) {
        for (int i = 0; i < engine.getUpgrades(u).size(); i++) {
            Button upgradeButton = new Button();
            Affector affector = engine.getUpgrades(u).get(i);
            upgradeButton.setOnMouseClicked(e -> engine.applyUpgrade(u, affector));
            upgradeButton.setText(affector.getClass().getSimpleName());
            upgrades.add(upgradeButton);
            upgradeBox.getChildren().add(upgradeButton);
        }
        upgradesBox.getChildren().add(upgradeBox);
    }
    
    public void addChildrenUpgrades(Unit tower) {
        for (int i = 0; i < tower.getChildren().size(); i++) {
        	HBox childUpgrades = new HBox(PANEL_SPACING);
            ImageView child =
                    new ImageView(new Image(tower.getChildren().get(i).toString() + ".png"));
            childUpgrades.getChildren().add(child);
            addUpgrades(tower.getChildren().get(i), childUpgrades);
            children.add(child);
        }
    }

    public void whenNothingSelected () {
    	HUDBox.getChildren().clear();
    	Rectangle rect = new Rectangle(500, 150);
    	rect.setFill(Color.BLACK);
    	HUDBox.getChildren().add(rect);
        if(gameView != null) {
            gameView.setSpecificUnitIsClicked(null);
        }
//        unitImage.setImage(new Image("Tower.png"));
//        unitType.setText("No Selection");
//        sellButton.setText(myResources.getString("HUDSellButton"));
    }
}

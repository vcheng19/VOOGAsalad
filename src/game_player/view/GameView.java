package game_player.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import exceptions.WompException;
import game_engine.physics.CollisionChecker;
import game_engine.physics.CollisionDetector;
import game_engine.GameEngineInterface;
import game_engine.game_elements.Branch;
import game_engine.game_elements.Unit;
import game_engine.properties.Position;
import game_player.GameDataSource;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


public class GameView implements IGameView {

    public final String[] seeRangeElements = { "Tower" };
    private int timer;
    private static final double DEFAULT_UPDATE_SPEED = 0.1;
    private AnimationTimer AT;
    private boolean timerStatus;
    private boolean isPlaying;
    private GameCanvas canvas;
    private Pane root;
    private Pane towerRoot;
    private Unit specificUnitIsClicked;
    private Shape unionRange;
    private Scene myScene;
    private GameEngineInterface playerEngineInterface;
    private GameDataSource gameData;
    private double myUpdateSpeed;
    private double currentSpeed;
    private List<ImageViewPicker> towers;
    private List<ImageViewPicker> enemies;
    private List<ImageViewPicker> projectiles;
    private List<ImageView> paths;
    private List<ImageViewPicker> terrains;
    private PlayerMainTab myTab;
    private List<ImageView> towerTypes;
    private String clickedTower;
    private boolean canPlaceTower;
    private GameHUD myHUD;
    private int moveSpeed;

    public GameView (GameEngineInterface engine,
                     GameCanvas canvas,
                     GameHUD hud,
                     Scene scene,
                     PlayerMainTab tab) {
        moveSpeed = 2;
        this.root = canvas.getRoot();
        this.myScene = scene;
        this.playerEngineInterface = engine;
        gameData = new GameDataSource();
        isPlaying = true;
        canPlaceTower = false;
        this.towers = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.terrains = new ArrayList<>();
        this.towerTypes = new ArrayList<>();
        this.timer = 0;
        this.unionRange = new Polygon();
        this.myUpdateSpeed = DEFAULT_UPDATE_SPEED;
        this.currentSpeed = DEFAULT_UPDATE_SPEED;
        this.paths = new ArrayList<>();
        this.myTab = tab;
        this.myHUD = hud;
        myHUD.setGameView(this);
        myHUD.setEngine(engine);
        setEventHandlers();
    }

    private void setEventHandlers () {
        this.myScene.setOnKeyPressed(e -> setUpKeyPressed(e.getCode()));
        this.root.setOnMouseClicked(e -> {
            if (clickedTower != null) {
                playerEngineInterface.addTower(clickedTower, e.getX(), e.getY());
            }
            else if (canPlaceTower) {
                myHUD.whenNothingSelected();
            }
            canPlaceTower = true;
        });
        this.root.setOnMouseMoved(e -> {
            playerEngineInterface.setCursorPosition(e.getX(), e.getY());
        });

    }

    public void setUpKeyPressed (KeyCode code) {
        switch (code) {
            case SPACE:
                toggleGame();
                break;
            case ESCAPE:
                clickedTower = null;
                break;
            case W:
                if(specificUnitIsClicked != null) {
                    playerEngineInterface.moveUnit(specificUnitIsClicked, specificUnitIsClicked.getProperties().getPosition().getX(),
                              specificUnitIsClicked.getProperties().getPosition().getY() - moveSpeed);
                }
                break;
            case A:
                if(specificUnitIsClicked != null) {
                    playerEngineInterface.moveUnit(specificUnitIsClicked, specificUnitIsClicked.getProperties().getPosition().getX() - moveSpeed,
                              specificUnitIsClicked.getProperties().getPosition().getY());
                }
                break;
            case D:
                if(specificUnitIsClicked != null) {
                    playerEngineInterface.moveUnit(specificUnitIsClicked, specificUnitIsClicked.getProperties().getPosition().getX() + moveSpeed,
                              specificUnitIsClicked.getProperties().getPosition().getY());
                }
                break;
            case S:
                if(specificUnitIsClicked != null) {
                    playerEngineInterface.moveUnit(specificUnitIsClicked, specificUnitIsClicked.getProperties().getPosition().getX(),
                              specificUnitIsClicked.getProperties().getPosition().getY() + moveSpeed);
                }
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void toggleGame () {
        if (timerStatus) {
            myUpdateSpeed = 0;
            timerStatus = false;
        }
        else {
            myUpdateSpeed = currentSpeed;
            timerStatus = true;
        }
    }

    public void display () {
        playGame(0);
    }

    @Override
    public void playGame (int gameIndex) {
        AT = new AnimationTimer() {
            public void handle (long currentNanoTime) {
                if (isPlaying) {
                    timer++;
                    updateEngine();
                    placePath();
                    placeUnits(playerEngineInterface.getTowers(), towers);
                    placeUnits(playerEngineInterface.getTerrains(), terrains);
                    placeUnits(playerEngineInterface.getProjectiles(), projectiles);
                    placeUnits(playerEngineInterface.getEnemies(), enemies);
                    // makeTowerPicker();
                }
            }
        };
        AT.start();
        timerStatus = true;
    }

    @Override
    public void restartGame () {
        // restart Game
    }

    private void updateEngine () {
        for (int i = 1; i <= myUpdateSpeed; i++) {
            playerEngineInterface.update();
        }
        myTab.updateGameElements();
    }

    @Override
    public void changeColorScheme (int colorIndex) {
        // TODO Auto-generated method stub
    }

	public void placePath () {
		List<Branch> currBranches = new ArrayList<>();
		// Comment this out to hide path grid
		currBranches.addAll(playerEngineInterface.getBranches());
		List<Position> allPositions = new ArrayList<>();
		currBranches.stream().forEach(cb -> allPositions.addAll(cb.getPositions()));
		for(int i = paths.size(); i < allPositions.size(); i++) {
			Image img = new Image("DirtNew.png");
			ImageView imgView = new ImageView(img);
			imgView.setX(allPositions.get(i).getX() - imgView.getImage().getWidth()/2);
			imgView.setY(allPositions.get(i).getY() - imgView.getImage().getHeight()/2);
			root.getChildren().add(imgView);
			imgView.toFront();
			paths.add(imgView);
		}
	}
	
    @Override
    public void changeGameSpeed (double gameSpeed) {
        this.myUpdateSpeed = gameSpeed;
        this.currentSpeed = gameSpeed;
    }

    public void makeTowerPicker () {
        List<Unit> allTowerTypes = playerEngineInterface.getTowerTypes();
        for (int i = towerTypes.size(); i < allTowerTypes.size(); i++) {
            String name = allTowerTypes.get(i).toString();
            Image img = new Image(name + ".png");
            ImageView imgView = new ImageView(img);
            imgView.setX(100 * i);
            imgView.setY(0);
            imgView.setRotate(transformDirection(allTowerTypes.get(i)));
            towerTypes.add(imgView);
            myTab.addToConfigurationPanel(imgView);
            imgView.setOnMouseClicked(e -> clickedTower = name);
        }
    }

    public void placeUnits (List<Unit> totalList, List<ImageViewPicker> imageViews) {
        List<Unit> list =
                totalList.stream().filter(u -> u.isVisible()).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            if (!hasImageView(list.get(i), imageViews)) {
                ImageViewPicker picker = new ImageViewPicker(list.get(i), root);
                picker.getImageView().setOnMouseClicked(e -> updateHUD(picker));
                imageViews.add(picker);
            }
        }
        for (int i = 0; i < imageViews.size(); i++) {
            ImageViewPicker picker = imageViews.get(i);
            if (imageViews.get(i).getUnit().isVisible()) {
                picker.selectNextImageView(timer);
            }
            else {
                picker.removeElementsFromRoot();
                imageViews.remove(i);
            }
        }
        displayRange(towers);
    }

    public void updateHUD (ImageViewPicker imager) {
        myHUD.whenTowerSelected(imager.getUnit());
        canPlaceTower = false;
    }

    public void displayRange (List<ImageViewPicker> imageViews) {
        List<Polygon> ranges = new ArrayList<>();
        List<Polygon> rangesToSubtract = new ArrayList<>();
        for (int i = 0; i < imageViews.size(); i++) {
            boolean seeRange = imageViews.get(i).getUnit().toString().contains(seeRangeElements[0]);
            if (seeRange) {
                Unit myUnit = imageViews.get(i).getUnit();
                List<Position> bounds =
                		CollisionChecker.getUseableBounds(myUnit.getChildren().get(0)
                                .getProperties().getBounds(),
                                                           myUnit.getProperties().getPosition());
                List<Position> range =
                        CollisionChecker.getUseableBounds(myUnit.getChildren().get(0)
                                .getProperties().getRange(),
                                                           myUnit.getProperties().getPosition());
                rangesToSubtract.add(fillPolygonWithPoints(new Polygon(), bounds));
                ranges.add(fillPolygonWithPoints(new Polygon(), range));
            }
        }
        root.getChildren().remove(unionRange);
        if (ranges.size() > 0) {
            unionRange = generateUnion(ranges);
            unionRange = subtractBounds(unionRange, rangesToSubtract);
            unionRange.setFill(Color.BLACK);
            unionRange.toFront();
            unionRange.setOpacity(0.1);
            root.getChildren().add(unionRange);
        }
    }

    public Shape generateUnion (List<Polygon> polygons) {
        Shape first = polygons.get(0);
        if (polygons.size() == 1) {
            return first;
        }
        for (int i = 1; i < polygons.size(); i++) {
            first = Shape.union(first, polygons.get(i));
        }
        return first;
    }

    public Shape subtractBounds (Shape first, List<Polygon> bounds) {
        for (int i = 0; i < bounds.size(); i++) {
            first = Shape.subtract(first, bounds.get(i));
        }
        return first;
    }

    public Polygon fillPolygonWithPoints (Polygon polygon, List<Position> points) {
        for (Position p : points) {
            polygon.getPoints().addAll(new Double[] { p.getX(), p.getY() });
        }
        return polygon;
    }

    public boolean hasImageView (Unit u, List<ImageViewPicker> imageViews) {
        for (int i = 0; i < imageViews.size(); i++) {
            if (imageViews.get(i).getUnit() == u) {
                return true;
            }
        }
        return false;
    }

    public double transformDirection (Unit u) {
        return -u.getProperties().getVelocity().getDirection() + 90;
    }

    @Override
    public GameEngineInterface getGameEngine () {
        return playerEngineInterface;
    }

    @Override
    public void changeClickedTower (String name) {
        clickedTower = name;
    }

    @Override
    public void setSpecificUnitIsClicked (Unit unit) {
        this.specificUnitIsClicked = unit;

    }

}

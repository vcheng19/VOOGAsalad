package game_player.view;

import java.util.ResourceBundle;

import game_player.GameDataSource;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class GameSpeedSlider implements IGUIObject{
	private static final float TICK_UNITS = 0.5f;
	private static final int SLIDER_DEFAULT = 1;
	private static final int SLIDER_MIN = 0;
	private static final double SLIDER_MAX = 2;
	private static final int PADDING = 10;
	private static final int VBOX_SPACING = 5;
	private ResourceBundle myResources;
	
	public GameSpeedSlider(ResourceBundle r, GameDataSource gameData) {
		myResources = r;
	}
	
	@Override
	public Node createNode() {
		VBox sliderBox = new VBox(VBOX_SPACING);
		
		Label sliderLabel = new Label(myResources.getString("AnimationSlider"));
		Slider animationSpeed = new Slider();
		animationSpeed.setMin(SLIDER_MIN);
		animationSpeed.setMax(SLIDER_MAX);
		animationSpeed.setValue(SLIDER_DEFAULT);
		animationSpeed.setMajorTickUnit(TICK_UNITS);
		animationSpeed.setShowTickMarks(true);
		animationSpeed.setShowTickLabels(true);
		
		animationSpeed.valueProperty().addListener(event -> setSpeed(animationSpeed.getValue()));
		
		sliderBox.setAlignment(Pos.TOP_CENTER);
		sliderBox.getChildren().addAll(sliderLabel, animationSpeed);
		sliderBox.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		return sliderBox;
	}
	
	private void setSpeed(double value) {
		//figure out how to change animation speed from here
	}

	@Override
	public void updateNode() {
		// TODO Auto-generated method stub
		
	}
	
}